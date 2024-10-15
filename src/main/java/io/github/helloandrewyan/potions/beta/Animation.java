package io.github.helloandrewyan.potions.beta;

import io.github.helloandrewyan.potions.enums.Heat;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.data.Levelled;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;
import io.github.helloandrewyan.potions.Potions;
import io.github.helloandrewyan.potions.cauldron.Cauldron;

import java.util.Random;

public class Animation {

		public static final int TICKS_PER_SECOND = 20;

		public static void playCauldronRegister(Location location) {
				World world = location.getWorld();
				Location center = location.toCenterLocation();
				world.spawnParticle(Particle.WATER_SPLASH, center, 14, 0.05, 2, 0.05, 1);
				playSounds(world, location, Sound.ENTITY_ITEM_FRAME_BREAK, Sound.AMBIENT_UNDERWATER_ENTER);

				Block cauldron = center.getBlock();
				if (cauldron.getType().equals(Material.CAULDRON)) {
						cauldron.setType(Material.WATER_CAULDRON);
				}
				if (cauldron.getType().equals(Material.WATER_CAULDRON)) {
						Levelled data = (Levelled) cauldron.getBlockData();
						Bukkit.getScheduler().runTaskTimer(Potions.getInstance(), (task) -> {
								if (data.getLevel() == data.getMaximumLevel()) {
										task.cancel();
								}
								data.setLevel(Math.min(3, data.getLevel() + 1));
								cauldron.setBlockData(data);
						}, 0L, 2L);
				}
				playHeatLoop(location);
		}

		public static void playCauldronUnregister(Location location) {
				World world = location.getWorld();
				Location center = location.toCenterLocation();
				world.spawnParticle(Particle.WATER_SPLASH, center, 5);
				playSounds(world, location, Sound.AMBIENT_UNDERWATER_EXIT);

				Block cauldron = location.getBlock();
				Material material = cauldron.getType();
				if (material.equals(Material.WATER_CAULDRON)) {
						cauldron.setType(Material.CAULDRON);
				}
		}

		public static void playToggleHeat(Location location, Heat heat) {
				World world = location.getWorld();
				Location center = location.toCenterLocation();
				world.spawnParticle(Particle.CRIT, center, 2, 0.5, 0.5, 0.5, 0.5);
				playSounds(world, location, Sound.ENTITY_BLAZE_SHOOT);

				TextComponent component = Component.text(heat.name()).color(heat.getColor());
				Vector velocity = new Vector(0, 0.25 + 0.05 * (heat.ordinal() + 1), 0);
				double duration = 0.4 + 0.2 * (heat.ordinal() + 1);
				summonItemNotification(center, Material.BLAZE_POWDER, component, velocity, duration);
		}

		public static void playSkip(Location location, int count) {World world = location.getWorld();
				Location center = location.toCenterLocation();
				world.spawnParticle(Particle.WATER_SPLASH, center, 14, 0.05, 1, 0.05, 0.5);
				playSounds(world, location, Sound.ENTITY_EVOKER_CAST_SPELL);

				TextComponent component = Component.text("Step " + (count));
				Vector velocity = new Vector(0, 0.5, 0);
				summonItemNotification(center, Material.CLOCK, component, velocity, 1.2);
		}

		public static void playComplete(Location location, ItemStack item) {
				World world = location.getWorld();
				Location center = location.toCenterLocation();
				world.playSound(center, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
				world.spawnParticle(Particle.VILLAGER_HAPPY, center, 10, 0.5, 0.5, 0.5, 0.5);
				playSounds(world, location, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, Sound.BLOCK_BREWING_STAND_BREW);

				TextComponent component = (TextComponent) item.getItemMeta().displayName();
				Vector velocity = new Vector(0, 0.3, 0);
				summonItemNotification(location, item.getType(), component, velocity, 0.7);
		}

		public static void playProgress(Location location) {
				World world = location.getWorld();
				Location center = location.toCenterLocation();
				world.spawnParticle(Particle.VILLAGER_HAPPY, center, 10, 0.5, 0.5, 0.5, 0.5);
				playSounds(world, location, Sound.BLOCK_BUBBLE_COLUMN_BUBBLE_POP);
		}

		public static void playMistake(Location location) {
				World world = location.getWorld();
				Location center = location.toCenterLocation();
				world.spawnParticle(Particle.FLASH, center, 1);
				world.spawnParticle(Particle.REDSTONE, center, 30, 0.5, 2, 0.5, 0.05,
								new Particle.DustOptions(Color.MAROON, 2));
				world.spawnParticle(Particle.SMOKE_LARGE, center, 30, 0.5, 2, 0.5, 0.05);
				world.spawnParticle(Particle.CAMPFIRE_COSY_SMOKE, center, 60, 0.5, 2, 0.5, 0.05);
				playSounds(world, location, Sound.ENTITY_CREEPER_PRIMED, Sound.ENTITY_GENERIC_EXPLODE);

				for (Entity entity : location.getNearbyEntities(3, 3, 3)) {
						if (!(entity instanceof LivingEntity livingEntity)) {
								continue;
						}
						Location entityLocation = livingEntity.getLocation();
						Vector direction = center.subtract(entityLocation).toVector();

						double distance = Math.max(1, location.distance(entityLocation));
						direction.multiply(1 / distance).normalize().multiply(-1);
						direction.setY(1.2 / distance);

						livingEntity.setVelocity(direction);
						livingEntity.damage((new Random().nextInt(8) + 6) / distance);
						livingEntity.addPotionEffect(
										new PotionEffect(PotionEffectType.BLINDNESS, 40, 1));
				}

				TextComponent component = Component.text("!! FAILED !!").color(TextColor.fromHexString("#ad1652"));
				Vector velocity = new Vector(0, 0.6, 0);
				summonItemNotification(location, Material.BARRIER, component, velocity, 1.5);
		}

		// -----------------------------------------------------------------------------------------------------------------

		private static void playHeatLoop(Location location) {
				World world = location.getWorld();
				Location center = location.toCenterLocation();
				Location bottom = location.toCenterLocation().subtract(0, 0.25, 0);
				Bukkit.getScheduler().runTaskTimer(Potions.getInstance(), (task) -> {
						Cauldron cauldron = Potions.getCauldrons().getCauldron(location);
						if (cauldron == null) {
								task.cancel();
								return;
						}

						switch (cauldron.getHeat()) {
								case LOW -> {
										world.spawnParticle(Particle.FLAME, bottom, 1, .25, 0, .25, .01);
										world.spawnParticle(Particle.SMOKE_NORMAL, center, 3, 0, 0, 0, 0.1);
								}
								case MEDIUM -> {
										world.spawnParticle(Particle.FLAME, bottom, 2, .25, 0, .25,.02);
										world.spawnParticle(Particle.SMOKE_LARGE, center, 3, 0, 0, 0, 0.1);
								}
								case HIGH -> {
										world.spawnParticle(Particle.FLAME, bottom, 3, .25, 0, .25, .03);
										world.spawnParticle(Particle.CAMPFIRE_COSY_SMOKE, center, 3, 0, 0, 0, 0.1);
								}
						}
				}, 0L, 1L);
		}

		private static void summonItemNotification(Location location, Material material,
																							 Component message, Vector velocity, double duration) {
				World world = location.getWorld();
				Location center = location.toCenterLocation();
				ItemStack itemStack = new ItemStack(material);

				Item notification = world.spawn(center, Item.class);
				notification.setItemStack(itemStack);
				notification.setCanPlayerPickup(false);
				notification.setCanMobPickup(false);
				notification.customName(message);
				notification.setCustomNameVisible(true);
				notification.setVelocity(velocity);

				Runnable task = notification::remove;
				Bukkit.getScheduler().runTaskLater(Potions.getInstance(), task, (long) (TICKS_PER_SECOND * duration));
		}

		private static void playSounds(World world, Location location, Sound... sounds) {
				for (Sound sound : sounds) {
						world.playSound(location, sound, 1, 1);
				}
		}
}

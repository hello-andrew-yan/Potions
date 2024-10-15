package io.github.helloandrewyan.potions.step;

import io.github.helloandrewyan.potions.Potions;
import io.github.helloandrewyan.potions.cauldron.Cauldron;
import io.github.helloandrewyan.potions.enums.Result;
import io.github.helloandrewyan.potions.resource.Recipe;
import net.kyori.adventure.text.Component;
import org.bukkit.*;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public abstract class Step {

		protected static int TICKS_PER_SECOND = 20;

		protected Recipe recipe;

		public Step() {
				this.recipe = null;
		}

		public void setRecipe(Recipe recipe) {
				this.recipe = recipe;
		}

		public abstract void executeStep(Cauldron cauldron);

		public abstract Result getResult(Step step);

		public abstract Step copy();

		// Change this to Component later.
		public abstract String getStepString();

		protected static void summonItemNotification(Location location, Material material,
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

		protected static void playSounds(World world, Location location, Sound... sounds) {
				for (Sound sound : sounds) {
						world.playSound(location, sound, 1, 1);
				}
		}
}

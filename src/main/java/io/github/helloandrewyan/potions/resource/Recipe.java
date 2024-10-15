package io.github.helloandrewyan.potions.resource;

import io.github.helloandrewyan.potions.cauldron.Cauldron;
import io.github.helloandrewyan.potions.enums.Result;
import io.github.helloandrewyan.potions.utility.Item;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.persistence.PersistentDataType;
import io.github.helloandrewyan.potions.step.Step;

import java.util.LinkedList;
import java.util.Queue;


public class Recipe extends Resource {

		public static int SETBACK_CONSTANT = 2;

		private final Queue<Step> steps;

		private int completedSteps;

		private Cauldron cauldron;

		public Recipe(String name, String description, String hex, Queue<Step> steps) {
				super(name, description, hex);
				this.steps = steps;
				this.steps.forEach(step -> step.setRecipe(this));
				this.completedSteps = 0;
				this.cauldron = null;
		}

		public Step getCurrentStep() {
				return steps.peek();
		}

		// Needs to be a separate function because Steps have alternative
		// timings in their animation before returning the function.
		public void handleResult(Result result) {
				if (result == null || cauldron == null) return;
				if (result == Result.COMPLETE) {
						steps.poll();
						completedSteps++;
				}
				if (result == Result.FAILED) {
						result = (completedSteps > SETBACK_CONSTANT) ? Result.SALVAGED : Result.FAILED;
						cauldron.handleResult(result);
						return;
				}
				if (steps.isEmpty()) cauldron.handleResult(Result.COMPLETE);
				else cauldron.handleResult(Result.PROGRESS);
		}

		public void setCauldron(Cauldron cauldron) {
				this.cauldron = cauldron;
		}

		public Recipe getCopy() {
				Queue<Step> copy = new LinkedList<>();
				steps.forEach(step -> copy.add(step.copy()));
				return new Recipe(name, description, hex != null ? hex.asHexString() : null, copy);
		}

		@Override
		public ItemStack getItemStack() {
				ItemStack item = Item.getItemStack(name, description, hex);
				item.setType(Material.POTION);

				// JSON Schema Validation would prevent incorrectly set Ingredient classes.
				PotionMeta meta = (PotionMeta) item.getItemMeta();
				assert hex != null;
				meta.setColor(Color.fromRGB(hex.value()));
				meta.addEnchant(Enchantment.LOOT_BONUS_BLOCKS, 10, true);
				meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
				meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
				item.setItemMeta(meta);

				return item;
		}

		public ItemStack getSalvagedItemStack() {
				ItemStack item = Item.getItemStack(name, description, hex);
				item.setType(Material.POTION);

				// JSON Schema Validation would prevent incorrectly set Ingredient classes.
				PotionMeta meta = (PotionMeta) item.getItemMeta();
				assert hex != null;
				meta.setColor(Color.fromRGB(hex.value()));
				meta.displayName(Item.getFormattedName("Salvaged " + name, hex));

				int skippedSteps = completedSteps - SETBACK_CONSTANT;
				meta.lore(Item.getFormattedLore("This recipe starts at step " + skippedSteps, hex));
				meta.getPersistentDataContainer().set(Item.POTIONS_RECIPE_INDEX_KEY, PersistentDataType.INTEGER, skippedSteps);
				item.setItemMeta(meta);

				return item;
		}

		public void skipSteps(int count) {
				while (count-- > 0 && !steps.isEmpty()) steps.poll();
		}
}

package io.github.helloandrewyan.potions.step.type;

import io.github.helloandrewyan.potions.Potions;
import io.github.helloandrewyan.potions.cauldron.Cauldron;
import io.github.helloandrewyan.potions.enums.Result;
import org.bukkit.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Vector;
import io.github.helloandrewyan.potions.resource.Recipe;
import io.github.helloandrewyan.potions.step.Step;
import io.github.helloandrewyan.potions.utility.Item;

/**
 * The type Ingredient step.
 */
public class IngredientStep extends Step {

		private static final double ADD_INGREDIENT_DELAY = 0.3;
		private final ItemStack ingredient;
		private int count;

		public IngredientStep(ItemStack ingredient, int count) {
				super();
				this.ingredient = ingredient;
				this.count = count;
		}

		public ItemStack getIngredient() {
				return ingredient;
		}

		public int getCount() {
				return count;
		}

		@Override
		public void executeStep(Cauldron cauldron) {
				Location center = cauldron.getLocation().toCenterLocation();
				Location start = center.add(0, 1, 0);
				Bukkit.getScheduler().runTaskLater(Potions.getInstance(), () -> {
						World world = cauldron.getLocation().getWorld();
						world.playSound(start, Sound.BLOCK_BREWING_STAND_BREW, 1, 1);
						world.spawnParticle(Particle.WATER_SPLASH, center, 14, 0.05, 1, 0.05, 0.5);

						Recipe recipe = cauldron.getRecipe();
						if (recipe != null) {
								recipe.handleResult(recipe.getCurrentStep().getResult(this));
								return;
						}

						PersistentDataContainer container = ingredient.getItemMeta().getPersistentDataContainer();
						if (container.has(Item.POTIONS_RECIPE_INDEX_KEY)) {
								String data = container.get(Item.POTIONS_NAME_KEY, PersistentDataType.STRING);
								cauldron.setRecipe(Potions.getRecipes().getRecipe(data));

								Integer count = container.get(Item.POTIONS_RECIPE_INDEX_KEY, PersistentDataType.INTEGER);
								if (count != null) {
										cauldron.skipSteps(count);
								}
						}
				}, (long) (ADD_INGREDIENT_DELAY * TICKS_PER_SECOND));

				ItemStack item = ingredient;
				Vector velocity = new Vector(0, -0.2, 0);
				summonItemNotification(start, item.getType(), item.getItemMeta().displayName(), velocity, ADD_INGREDIENT_DELAY);
		}

		@Override
		public Result getResult(Step step) {
				if (!(step instanceof IngredientStep ingredientStep)) return Result.FAILED;
				if (!ingredientStep.getIngredient().equals(ingredient)) return Result.FAILED;

				int newCount = count - ingredientStep.getCount();
				if (newCount < 0) return Result.FAILED;
				if (newCount > 0) {
						count = newCount;
						return Result.PROGRESS;
				}

				return Result.COMPLETE;
		}

		@Override
		public Step copy() {
				return new IngredientStep(ingredient, count);
		}

		@Override
		public String getStepString() {
				return "Add " + count + " " + ingredient.getItemMeta().displayName();
		}
}

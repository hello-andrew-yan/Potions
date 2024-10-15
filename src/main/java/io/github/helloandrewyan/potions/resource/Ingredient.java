package io.github.helloandrewyan.potions.resource;

import io.github.helloandrewyan.potions.Potions;
import io.github.helloandrewyan.potions.enums.Method;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import io.github.helloandrewyan.potions.beta.tool.Tools;
import io.github.helloandrewyan.potions.utility.Item;

import java.util.HashMap;

/**
 * The type Ingredient.
 */
public class Ingredient extends Resource {

		private final Material material;
		private final int customModelData;
		private final HashMap<Method, String> subIngredients;

		public Ingredient(String name, String description, String hex, String material, int customModelData, HashMap<Method, String> subIngredients) {
				super(name, description, hex);

				Material fetchMaterial = Material.getMaterial(material);
				this.material = fetchMaterial != null ? fetchMaterial: Item.DEFAULT_MATERIAL;
				this.customModelData = customModelData;
				this.subIngredients = subIngredients;
		}

		@Override
		public ItemStack getItemStack() {
				ItemStack item = Item.getItemStack(name, description, hex);
				item.setType(material);

				ItemMeta meta = item.getItemMeta();
				meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
				meta.setCustomModelData(customModelData);
				item.setItemMeta(meta);

				return item;
		}

		public void addSubIngredientRecipes() {
				subIngredients.forEach((method, ingredient) -> {
						NamespacedKey key = new NamespacedKey(Potions.getInstance(), ingredient.replaceAll(" ", "_"));
						ShapelessRecipe recipe = new ShapelessRecipe(key, Potions.getIngredients().getIngredient(ingredient));
						recipe.addIngredient(getItemStack());

						switch (method) {
								case CUT -> recipe.addIngredient(Tools.getCuttingTool());
								case CRUSH -> recipe.addIngredient(Tools.getCrushingTool());
								case STEW -> recipe.addIngredient(Tools.getStewingTool());
						}

						Bukkit.addRecipe(recipe);
						Potions.info(name + ": Adding subingredient \"" + ingredient + "\" from " + method.name());
				});

		}
}

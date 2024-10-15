package io.github.helloandrewyan.potions.manager;

import io.github.helloandrewyan.potions.enums.Method;
import io.github.helloandrewyan.potions.resource.Ingredient;
import org.bukkit.inventory.ItemStack;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * The {@code Ingredients} class is a concrete implementation of the {@code Manager} class
 * specifically for managing {@link Ingredient} resources.
 * <p>
 * This class is responsible for adding {@code Ingredient} objects to its internal list
 * based on JSON data read from files in the specified data directory. The JSON data
 * is validated against a schema before being processed.
 * </p>
 */
public class Ingredients extends Manager<Ingredient> {

		/**
		 * Instantiates a new Ingredients.
		 *
		 * @param dataDirectory the data directory
		 * @param schemaPath    the schema path
		 */
		public Ingredients(String dataDirectory, String schemaPath) {
				super(dataDirectory, schemaPath);
		}

		/**
		 * Adds an {@code Ingredient} resource to the internal list of resources.
		 * <p>
		 * This method extracts the necessary data from the provided {@code JSONObject}
		 * and creates a new {@code Ingredient} instance, which is then added to the list
		 * of managed resources.
		 * </p>
		 *
		 * @param resource the {@code JSONObject} representing the ingredient data
		 */
		@Override
		protected void addResource(JSONObject resource) {

				HashMap<Method, String> subIngredients = new HashMap<>();
				JSONArray array = null;
				try {
						array = resource.getJSONArray("subingredient");
				} catch (JSONException ignored) {}
				if (array != null) {
						array.forEach(subingredient -> {
								subIngredients.put(
												((JSONObject) subingredient).getEnum(Method.class, "method"),
												((JSONObject) subingredient).getString("ingredient")
								);
						});
				}
				resources.put(resource.getString("name"),
								new Ingredient(
												resource.getString("name"),
												resource.getString("description"),
												resource.getString("hex"),
												resource.getString("material"),
												resource.getInt("customModelData"),
												subIngredients
								)
				);
		}

		public ItemStack getIngredient(String ingredient) {
				Ingredient result = resources.get(ingredient);
				return result != null ? result.getItemStack() : null;
		}

		public void loadSubIngredients() {
				resources.forEach((key, resource) -> resource.addSubIngredientRecipes());
		}
}


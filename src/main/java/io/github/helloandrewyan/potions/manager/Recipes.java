package io.github.helloandrewyan.potions.manager;

import io.github.helloandrewyan.potions.resource.Recipe;
import org.json.JSONObject;
import io.github.helloandrewyan.potions.step.Step;
import io.github.helloandrewyan.potions.step.StepFactory;

import java.util.*;

/**
 * The {@code Recipes} class is a concrete implementation of the {@code Manager} class
 * specifically for managing {@link Recipe} resources.
 * <p>
 * This class is responsible for adding {@code Recipe} objects to its internal list
 * based on JSON data read from files in the specified data directory. The JSON data
 * is validated against a schema before being processed.
 * </p>
 */
public class Recipes extends Manager<Recipe> {
		/**
		 * Instantiates a new Recipes.
		 *
		 * @param dataDirectory the data directory
		 * @param schemaPath    the schema path
		 */
		public Recipes(String dataDirectory, String schemaPath) {
				super(dataDirectory, schemaPath);
		}

		/**
		 * Adds a {@code Recipe} resource to the internal list of resources.
		 * <p>
		 * This method extracts the necessary data from the provided {@code JSONObject}
		 * and creates a new {@code Recipe} instance, which is then added to the list
		 * of managed resources.
		 * </p>
		 *
		 * @param resource the {@code JSONObject} representing the recipe data
		 */
		@Override
		protected void addResource(JSONObject resource) {

				Queue<Step> steps = new LinkedList<>();
				for (Object step : resource.getJSONArray("steps")) {
						if (step instanceof JSONObject jsonObject) {
								steps.add(StepFactory.getStep(jsonObject));
						}
				}

				Recipe recipe = new Recipe(
								resource.getString("name"),
								resource.getString("description"),
								resource.getString("hex"),
								steps
				);

				resources.put(resource.getString("name"), recipe);
		}

		public Recipe getRecipe(String name) {
				Recipe result = resources.get(name);
				return result != null ? result.getCopy() : null;
		}
}

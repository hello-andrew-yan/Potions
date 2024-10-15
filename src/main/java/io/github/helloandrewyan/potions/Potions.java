package io.github.helloandrewyan.potions;

import io.github.helloandrewyan.potions.listener.CauldronListener;
import io.github.helloandrewyan.potions.listener.CraftingListener;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import io.github.helloandrewyan.potions.beta.TestingListener;
import io.github.helloandrewyan.potions.cauldron.Cauldrons;
import io.github.helloandrewyan.potions.manager.Ingredients;
import io.github.helloandrewyan.potions.manager.Recipes;

import java.util.Arrays;

/**
 * The main class for the Potions plugin. It extends {@link JavaPlugin} and
 * serves as the entry point for the plugin.
 * <p>
 * This class handles plugin initialization, event registration, and provides
 * access to various plugin components such as ingredients, recipes, and cauldrons.
 * </p>
 */
public final class Potions extends JavaPlugin {

		private static Potions instance;

		private static Ingredients ingredients;
		private static Recipes recipes;
		private static Cauldrons cauldrons;

		private static final String INGREDIENT_DIRECTORY = "ingredients";
		private static final String INGREDIENT_SCHEMA_PATH = "schema/ingredient.schema.json";
		private static final String RECIPE_DIRECTORY = "recipes";
		private static final String RECIPE_SCHEMA_PATH = "schema/recipe.schema.json";

		private static final TextColor INFO = TextColor.fromHexString("#39d680");
		private static final TextColor WARNING = TextColor.fromHexString("#e24a61");


		@Override
		public void onEnable() {
				instance = this;

				ingredients = new Ingredients(INGREDIENT_DIRECTORY, INGREDIENT_SCHEMA_PATH);
				ingredients.loadSubIngredients();
				recipes = new Recipes(RECIPE_DIRECTORY, RECIPE_SCHEMA_PATH);
				cauldrons = new Cauldrons();

				registerEvents(new CauldronListener(), new CraftingListener(), new TestingListener());
		}

		@Override
		public void onDisable() {
				// Cleanup Code
		}

		private void registerEvents(Listener... listeners) {
				Arrays.stream(listeners).forEach(listener ->
								Bukkit.getServer().getPluginManager().registerEvents(listener, this));
		}

		public static Plugin getInstance() {
				return instance;
		}

		public static Ingredients getIngredients() {
				return ingredients;
		}

		public static Recipes getRecipes() {
				return recipes;
		}

		public static Cauldrons getCauldrons() {
				return cauldrons;
		}

		public static void info(String message) {
				getInstance().getComponentLogger().info(Component.text(message).color(INFO));
		}

		public static void warn(String message) {
				getInstance().getComponentLogger().warn(Component.text(message).color(WARNING));
		}
}

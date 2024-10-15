package io.github.helloandrewyan.potions.utility;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * The {@code Item} class provides utility methods and constants for handling
 * item-related functionalities, such as formatting names and descriptions.
 * <p>
 * This class includes constants for default values and methods for creating
 * formatted text components for item names and descriptions.
 * </p>
 */
public class Item {

		public static final int DEFAULT_DESCRIPTION_LENGTH = 31;
		public static final Material DEFAULT_MATERIAL = Material.GRASS_BLOCK;
		public static final TextColor DEFAULT_OFFICIAL_COLOR = TextColor.fromHexString("#b986f0");
		public static final NamespacedKey POTIONS_NAME_KEY = new NamespacedKey("potions", "name");
		public static final NamespacedKey POTIONS_RECIPE_INDEX_KEY = new NamespacedKey("potions", "index");

		public static ItemStack getItemStack(String name, String description, TextColor hex) {
				ItemStack item = new ItemStack(Item.DEFAULT_MATERIAL);

				ItemMeta meta = item.getItemMeta();
				meta.displayName(Item.getFormattedName(name, hex));
				meta.lore(Item.getFormattedLore(description, hex));
				meta.getPersistentDataContainer().set(POTIONS_NAME_KEY, PersistentDataType.STRING, name);

				item.setItemMeta(meta);
				return item;
		}

		/**
		 * Segments a string into a list of substrings based on the default description
		 * length, considering word boundaries.
		 * <p>
		 * This method is used internally to break down long item descriptions into
		 * manageable lines for display purposes.
		 * </p>
		 *
		 * @param input	The string to be segmented
		 * @return 			A list of segmented strings
		 */
		private static List<String> segmentString(String input) {
				// This pattern is designed to segment a string based on a specified length, considering word boundaries.
				Pattern pattern = Pattern.compile(String.format("\\G\\s*(.{1,%d})(?:\\s|$)", Item.DEFAULT_DESCRIPTION_LENGTH));
				return pattern.matcher(input)
								.results()
								.map(matchResult -> matchResult.group(1))
								.collect(Collectors.toList());
		}

		/**
		 * Creates a {@link Component} representing a formatted name for an item.
		 * <p>
		 * The name is styled with the specified color and decorated with bold text,
		 * while italic decoration is disabled.
		 * </p>
		 *
		 * @param name	The name of the item
		 * @param color The color to apply to the name
		 * @return 			A {@link Component} with the formatted item name
		 */
		public static Component getFormattedName(String name, TextColor color) {
				return Component.text(name)
								.decoration(TextDecoration.ITALIC, false)
								.decorate(TextDecoration.BOLD)
								.color(color);
		}

		/**
		 * Creates a list of {@link Component} objects representing a formatted lore
		 * for an item.
		 * <p>
		 * The description is segmented into multiple lines based on the default
		 * description length. Each line is styled with the specified color. An
		 * additional line with the text "Official Potterverse Item" is appended at
		 * the end, styled with the default official color.
		 * </p>
		 *
		 * @param description The description of the item
		 * @param color 			The color to apply to the description
		 * @return A list of {@link Component} objects with the formatted item lore
		 */
		public static List<Component> getFormattedLore(String description, TextColor color) {
				List<Component> lore = new ArrayList<>();

				segmentString(description).forEach(
								string -> lore.add(Component.text(string)
												.decoration(TextDecoration.ITALIC, false)
												.color(color))
				);
				lore.add(Component.text("Official Potterverse Item")
								.decorate(TextDecoration.ITALIC)
								.color(DEFAULT_OFFICIAL_COLOR));

				return lore;
		}
}

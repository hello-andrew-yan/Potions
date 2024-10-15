package io.github.helloandrewyan.potions.resource;

import net.kyori.adventure.text.format.TextColor;
import org.bukkit.inventory.ItemStack;

public abstract class Resource {

		protected final String name;
		protected final String description;
		protected final TextColor hex;

		protected Resource(String name, String description, String hex) {
				this.name = name;
				this.description = description;
				this.hex = TextColor.fromHexString(hex);
		}

		public abstract ItemStack getItemStack();

		public String getName() {
				return name;
		}

		public String getDescription() {
				return description;
		}

		public TextColor getHex() {
				return hex;
		}
}

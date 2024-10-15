package io.github.helloandrewyan.potions.enums;

import net.kyori.adventure.text.format.TextColor;

public enum Heat {
		OFF (TextColor.fromHexString("#FFFFFF")),
		LOW (TextColor.fromHexString("#e8e051")),
		MEDIUM (TextColor.fromHexString("#e8b851")),
		HIGH (TextColor.fromHexString("#e85151"));

		private final TextColor color;

		Heat(TextColor color) {
				this.color = color;
		}

		public TextColor getColor() {
				return color;
		}
}

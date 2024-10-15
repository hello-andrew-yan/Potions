package io.github.helloandrewyan.potions.beta;

import com.google.common.annotations.Beta;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentBuilder;
import net.kyori.adventure.text.TextComponent;

import java.util.ArrayList;
import java.util.List;

@Beta
public class Guide {

		private static final int MAX_WIDTH = 16;
		private static final int MAX_HEIGHT = 14;

		private List<Component> segmentStringToPages(List<String> lines) {
				List<Component> pages = new ArrayList<>();
				ComponentBuilder<TextComponent, ?> page = Component.text();
				// Uses the fact that overflowed Components will go into a new line anyway.
				// We don't need to perform the newline operation manually.
				int currentHeight = 0;
				for (String line : lines) {
						// Calculates how many lines it needs to display this in a Minecraft book.
						int segmentHeight = (int) Math.ceil((double) line.length() / MAX_WIDTH);
						// Exceeded maximum currentHeight of page.
						if (currentHeight + segmentHeight > MAX_HEIGHT) {
								// Adds completed page to list and resets the Component.
								pages.add(page.build());
								page = Component.text();
								currentHeight = 0;
						}
						// Optionally add TextDecoration and TextColor here.
						page.append(Component.text(line + "\n"));
						currentHeight += segmentHeight;
				}
				// Checks whether the page still contains elements.
				if (currentHeight > 0) pages.add(page.build());
				return pages;
		}
}

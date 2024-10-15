package io.github.helloandrewyan.potions.beta.tool;

import io.github.helloandrewyan.potions.utility.Item;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.inventory.ItemStack;

public class Tools {
		public static ItemStack getCrushingTool() {
				ItemStack item = Item.getItemStack(
								"Mortar and Pestle",
								"Crushes your components down.",
								TextColor.fromHexString("#266d57")
				);
				item.setType(Material.IRON_PICKAXE);
				return item;
		}

		public static ItemStack getCuttingTool() {
				ItemStack item = Item.getItemStack(
								"Potions Knife",
								"Slices your components down.",
								TextColor.fromHexString("#266d57")
				);
				item.setType(Material.IRON_SWORD);
				return item;
		}

		public static ItemStack getStewingTool() {
				ItemStack item = Item.getItemStack(
								"Stewing Pot",
								"Stews down your components.",
								TextColor.fromHexString("#266d57")
				);
				item.setType(Material.CAULDRON);
				return item;
		}

		public static Sound getSound(ItemStack item) {
				if (item.equals(getCrushingTool())) return Sound.BLOCK_GRINDSTONE_USE;
				if (item.equals(getCuttingTool())) return Sound.BLOCK_GRASS_BREAK;
				if (item.equals(getStewingTool())) return Sound.BLOCK_BREWING_STAND_BREW;
				return Sound.ENTITY_EXPERIENCE_ORB_PICKUP;
		}

		public static boolean isToolItem(ItemStack item) {
				return (item.equals(getCrushingTool())
								|| item.equals(getCuttingTool())
								|| item.equals(getStewingTool())
				);
		}
}

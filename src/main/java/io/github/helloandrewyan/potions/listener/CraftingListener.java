package io.github.helloandrewyan.potions.listener;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import io.github.helloandrewyan.potions.beta.tool.Tools;
import io.github.helloandrewyan.potions.utility.Item;

public class CraftingListener implements Listener {

		@EventHandler
		public void onCraftEvent(CraftItemEvent event) {
				if (!(event.getWhoClicked() instanceof Player player)) return;
				ItemStack result = event.getRecipe().getResult();
				PersistentDataContainer container = result.getItemMeta().getPersistentDataContainer();
				if (!container.has(Item.POTIONS_NAME_KEY)) return;

				ItemStack[] matrix = event.getInventory().getMatrix();

				int quantity = 1;
				if (event.getAction().equals(InventoryAction.MOVE_TO_OTHER_INVENTORY)) {
						int minQuantity = 64;
						// Iterate through the matrix to find the item with the minimum quantity
						for (ItemStack item : matrix) {
								if (item != null && !Tools.isToolItem(item)) {

										int itemAmount = item.getAmount();
										if (itemAmount < minQuantity) {
												minQuantity = itemAmount;
										}
								}
						}
						quantity = minQuantity;
						player.getInventory().addItem(result.asQuantity(quantity));
				} else {
						if (player.getItemOnCursor().getType().equals(Material.AIR)) {
								player.setItemOnCursor(result);
						} else if (player.getItemOnCursor().isSimilar(result)) {
								player.setItemOnCursor(player.getItemOnCursor().add());
						} else {
								return;
						}
				}

				Sound sound = Sound.ENTITY_EXPERIENCE_ORB_PICKUP;
				for (int i = 0; i < matrix.length; i++) {
						ItemStack item = matrix[i];
						if (item == null) continue;
						if (!Tools.isToolItem(item)){
								// Reduce the quantity of the item by 1
								item.setAmount(item.getAmount() - quantity);

								// If the quantity becomes 0 or less, set the item to null
								if (item.getAmount() <= 0) {
										matrix[i] = null;
								} else {
										matrix[i] = item;
								}
						} else {
								sound = Tools.getSound(item);
						}
				}
				event.getInventory().setMatrix(matrix);
				player.playSound(player.getEyeLocation(), sound, 1, 1);
				event.setCancelled(true);
		}
}

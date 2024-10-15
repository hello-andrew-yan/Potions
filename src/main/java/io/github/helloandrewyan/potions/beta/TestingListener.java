package io.github.helloandrewyan.potions.beta;

import com.google.common.annotations.Beta;
import io.github.helloandrewyan.potions.Potions;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.event.player.PlayerAnimationType;
import org.bukkit.inventory.ItemStack;
import io.github.helloandrewyan.potions.beta.tool.Tools;

@Beta
public class TestingListener implements Listener {
		@EventHandler
		public void onSwing(PlayerAnimationEvent event) {
				Player player = event.getPlayer();
				ItemStack item = player.getInventory().getItemInMainHand();

				if (event.getAnimationType().equals(PlayerAnimationType.ARM_SWING)) {
						if (item.getType().equals(Material.STONE)) {
								Potions.getIngredients().reloadResources();
						}
						if (item.getType().equals(Material.COBBLESTONE)) {
								Potions.getIngredients().getResourceNames().forEach(ingredient -> player.getInventory()
												.addItem(Potions.getIngredients().getIngredient(ingredient)));
						}
						if (item.getType().equals(Material.BEACON)) {
								player.getInventory().addItem(
												Tools.getCrushingTool(),
												Tools.getCuttingTool(),
												Tools.getStewingTool()
								);
						}
				}
		}
}

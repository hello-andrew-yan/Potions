package io.github.helloandrewyan.potions.listener;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;
import io.github.helloandrewyan.potions.Potions;
import io.github.helloandrewyan.potions.cauldron.Cauldron;
import io.github.helloandrewyan.potions.enums.Direction;
import io.github.helloandrewyan.potions.step.type.HeatStep;
import io.github.helloandrewyan.potions.step.type.IngredientStep;
import io.github.helloandrewyan.potions.step.type.StirStep;
import io.github.helloandrewyan.potions.utility.Item;

public class CauldronListener implements Listener {

		@EventHandler
    public void onBlockPlace(BlockBreakEvent event) {
				// Removal of an active cauldron.
        Location location = event.getBlock().getLocation();
        if (Potions.getCauldrons().getCauldron(location) != null) Potions.getCauldrons().unregisterCauldron(location);
    }

		@EventHandler
    public void onIngredientThrow(PlayerDropItemEvent event) {
				// Validation of thrown item as ingredient.
				ItemStack item = event.getItemDrop().getItemStack();
				PersistentDataContainer container = item.getItemMeta().getPersistentDataContainer();
				if (!container.has(Item.POTIONS_NAME_KEY)) return;

				// Checks if item is thrown in the direction of a nearby block.
        Player player = event.getPlayer();
        Vector direction = player.getEyeLocation().getDirection();
        RayTraceResult result =  player.getWorld().rayTraceBlocks(player.getEyeLocation(), direction, 4);
        if (result == null) return;

				// Checks if that block is a CAULDRON / WATER_CAULDRON.
        Block interactedBlock = result.getHitBlock();
        if (interactedBlock == null) return;

        Material material = interactedBlock.getBlockData().getMaterial();
        if (!material.equals(Material.CAULDRON) && !material.equals(Material.WATER_CAULDRON)) return;

				// Ownership check.
				Location location = interactedBlock.getLocation();
				if (!Potions.getCauldrons().isOwner(location, player)) return;
				event.getItemDrop().remove();

				// Execute the step.
				Cauldron cauldron = Potions.getCauldrons().getCauldron(player);
				cauldron.executeStep(new IngredientStep(item, item.getAmount()));
    }

		@EventHandler
    public void onCauldronInteract(PlayerInteractEvent event) {
				// Players in CREATIVE mode should not interact with cauldrons.
        Player player = event.getPlayer();
				if (player.getGameMode().equals(GameMode.CREATIVE)) return;

				// Checking if the action is LEFT CLICK BLOCK.
				Block interactedBlock = event.getClickedBlock();
        if (!event.getAction().equals(Action.LEFT_CLICK_BLOCK)) return;
        if (interactedBlock == null) return;

        Location location = interactedBlock.getLocation();
        Material material = interactedBlock.getBlockData().getMaterial();
        if (!material.equals(Material.CAULDRON) && !material.equals(Material.WATER_CAULDRON)) return;

				// Event is only cancelled when it's specific items interacting with the cauldron.
        ItemStack item = player.getInventory().getItemInMainHand();
				if (Potions.getCauldrons().getCauldron(player) == null
								&& Potions.getCauldrons().getCauldron(location) == null) {

						if (item.getType().equals(Material.AIR)) {
								Potions.getCauldrons().registerCauldron(location, player);
								event.setCancelled(true);
						}
						return;
				}

				if (item.getType().equals(Material.BUCKET) && Potions.getCauldrons().isOwner(location, player)) {
						Potions.getCauldrons().unregisterCauldron(location, player);
						event.setCancelled(true);
						return;
				}

        Cauldron cauldron = Potions.getCauldrons().getCauldron(location);
        if (cauldron == null) return;
				switch (item.getType()) {
            // TESTING SET RECIPE
            case DIAMOND -> {
                location.getWorld().playSound(location, Sound.BLOCK_ANVIL_PLACE, 1, 1);
                cauldron.setRecipe(Potions.getRecipes().getRecipe("Forgetfulness Potion"));
						}
            case STICK -> {
								Direction direction = player.isSneaking() ? Direction.ANTICLOCKWISE : Direction.CLOCKWISE;
                cauldron.executeStep(new StirStep(direction, 1));
						}
						default -> {
								cauldron.toggleHeat();
								cauldron.executeStep(new HeatStep(cauldron.getHeat()));
						}
        }
				event.setCancelled(true);
		}
}

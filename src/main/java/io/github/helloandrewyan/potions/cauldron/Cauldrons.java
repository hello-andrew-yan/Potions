package io.github.helloandrewyan.potions.cauldron;

import io.github.helloandrewyan.potions.beta.Animation;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class Cauldrons {

		private final ConcurrentHashMap<Location, UUID> occupied;
		private final ConcurrentHashMap<UUID, Cauldron> cauldrons;

		public Cauldrons() {
				this.occupied = new ConcurrentHashMap<>();
				this.cauldrons = new ConcurrentHashMap<>();
		}

		public Cauldron getCauldron(Location location) {
				UUID uuid = occupied.get(location);
				if (uuid == null) return null;
				return cauldrons.get(uuid);
		}

		public Cauldron getCauldron(Player player) {
				return cauldrons.get(player.getUniqueId());
		}

		public boolean isOwner(Location location, Player player) {
				UUID uuid = occupied.get(location);
				return uuid != null && uuid.equals(player.getUniqueId());
		}

		public void registerCauldron(Location location, Player player) {
				if (isOwner(location, player)) return;
				if (getCauldron(location) != null) {
						player.sendMessage(Component.text("This is not your cauldron!"));
						return;
				}
				occupied.putIfAbsent(location, player.getUniqueId());
				cauldrons.putIfAbsent(player.getUniqueId(), new Cauldron(location, player));
				Animation.playCauldronRegister(location);
		}

		public void unregisterCauldron(Location location, Player player) {
				if (!isOwner(location, player)) {
						player.sendMessage(Component.text("This is not your cauldron!"));
						return;
				}
				occupied.remove(location);
				cauldrons.remove(player.getUniqueId());
				Animation.playCauldronUnregister(location);
		}

		public void unregisterCauldron(Location location) {
				UUID uuid = occupied.get(location);
				if (uuid == null) return;
				cauldrons.remove(uuid);
				occupied.remove(location);
				Animation.playCauldronUnregister(location);
		}
}

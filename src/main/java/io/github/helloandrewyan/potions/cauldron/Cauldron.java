package io.github.helloandrewyan.potions.cauldron;

import io.github.helloandrewyan.potions.beta.Animation;
import io.github.helloandrewyan.potions.enums.Heat;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import io.github.helloandrewyan.potions.Potions;
import io.github.helloandrewyan.potions.resource.Recipe;
import io.github.helloandrewyan.potions.step.Step;
import io.github.helloandrewyan.potions.enums.Result;

public class Cauldron {

		private final Player owner;
		private final Location location;
		private Heat heat;
		private Recipe recipe;

		private Result brewResult;

		public Cauldron(Location location, Player owner) {
				this.location = location;
				this.owner = owner;
				this.heat = Heat.OFF;
				this.recipe = null;
				this.brewResult = Result.PROGRESS;
		}

		public void executeStep(Step step) {
				step.executeStep(this);
		}

		public void handleResult(Result result) {
				if (brewResult == Result.FAILED || brewResult == Result.SALVAGED) return;
				switch (result) {
						case COMPLETE -> {
								owner.getInventory().addItem(recipe.getItemStack());
								Animation.playComplete(location, recipe.getItemStack());
						}
						case SALVAGED -> {
								owner.sendMessage(Component.text("You salvage the remnants of your brew..."));
								owner.getInventory().addItem(recipe.getSalvagedItemStack());
								Animation.playMistake(location);
						}
						case PROGRESS -> Animation.playProgress(location);
						case FAILED -> Animation.playMistake(location);
				}
				if (result != Result.PROGRESS) {
						Potions.getCauldrons().unregisterCauldron(location, getOwner());
				}
				brewResult = result;
		}

		public Location getLocation() {
				return location;
		}

		public Player getOwner() {
				return owner;
		}

		public Heat getHeat() {
				return heat;
		}

		public void toggleHeat() {
				switch (heat) {
						case OFF, HIGH -> heat = Heat.LOW;
						case LOW -> heat = Heat.MEDIUM;
						case MEDIUM -> heat = Heat.HIGH;
				}
				Animation.playToggleHeat(location, heat);
		}

		public Recipe getRecipe() {
				return recipe;
		}

		public void setRecipe(Recipe recipe) {
				this.recipe = recipe;
				this.recipe.setCauldron(this);

				this.heat = Heat.OFF;
				Animation.playToggleHeat(location, heat);
		}

		public void skipSteps(int count) {
				recipe.skipSteps(count);
				Animation.playSkip(location, count);
		}
}

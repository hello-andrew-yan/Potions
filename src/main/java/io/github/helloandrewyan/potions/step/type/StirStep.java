package io.github.helloandrewyan.potions.step.type;

import io.github.helloandrewyan.potions.cauldron.Cauldron;
import io.github.helloandrewyan.potions.enums.Direction;
import io.github.helloandrewyan.potions.enums.Result;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.*;
import org.bukkit.util.Vector;
import io.github.helloandrewyan.potions.resource.Recipe;
import io.github.helloandrewyan.potions.step.Step;

public class StirStep extends Step {

		private final Direction direction;
		private int rotations;

		public StirStep(Direction direction, int rotations) {
				super();
				this.direction = direction;
				this.rotations = rotations;
		}

		public Direction getDirection() {
				return direction;
		}

		public int getRotations() {
				return rotations;
		}

		@Override
		public void executeStep(Cauldron cauldron) {
				World world = cauldron.getLocation().getWorld();
				Location center = cauldron.getLocation().toCenterLocation();
				world.spawnParticle(Particle.WATER_SPLASH, center, 14, 0.3, 0.3, 0.3, 0.5);
				playSounds(world, center, Sound.ENTITY_BOAT_PADDLE_WATER);

				TextComponent component = Component.text(direction.name());
				Vector velocity = new Vector(0, 0.3, 0);
				summonItemNotification(center, Material.GHAST_TEAR, component, velocity, 0.7);

				Recipe recipe = cauldron.getRecipe();
				if (recipe == null) return;
				recipe.handleResult(recipe.getCurrentStep().getResult(this));
		}

		@Override
		public Result getResult(Step step) {
				if (!(step instanceof StirStep stirStep)) return Result.FAILED;
				if (!stirStep.getDirection().equals(direction)) return Result.FAILED;

				int newCount = stirStep.getRotations();
				if (newCount < 0) return Result.FAILED;
				if (newCount > 0) {
						rotations = newCount;
						return Result.PROGRESS;
				}

				return Result.COMPLETE;
		}

		@Override
		public Step copy() {
				return new StirStep(direction, rotations);
		}

		@Override
		public String getStepString() {
				return "Stir " + rotations + " time " + direction.toString();
		}
}

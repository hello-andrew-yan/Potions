package io.github.helloandrewyan.potions.step.type;

import io.github.helloandrewyan.potions.cauldron.Cauldron;
import io.github.helloandrewyan.potions.enums.Heat;
import io.github.helloandrewyan.potions.enums.Result;
import io.github.helloandrewyan.potions.resource.Recipe;
import io.github.helloandrewyan.potions.step.Step;

public class HeatStep extends Step {

		private final Heat heat;

		public HeatStep(Heat heat) {
				super();
				this.heat = heat;
		}

		public Heat getHeat() {
				return heat;
		}

		@Override
		public void executeStep(Cauldron cauldron) {
				Recipe recipe = cauldron.getRecipe();
				if (recipe == null) return;
				recipe.handleResult(recipe.getCurrentStep().getResult(this));
		}

		@Override
		public Result getResult(Step step) {
				if (!(step instanceof HeatStep heatStep)) return Result.FAILED;
				if (heatStep.getHeat() != heat)  return Result.PROGRESS;
				return Result.COMPLETE;
		}

		@Override
		public Step copy() {
				return new HeatStep(heat);
		}

		@Override
		public String getStepString() {
				return "Set heat to " + heat.toString();
		}
}

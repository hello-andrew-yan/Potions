package io.github.helloandrewyan.potions.step.type;

import io.github.helloandrewyan.potions.cauldron.Cauldron;
import io.github.helloandrewyan.potions.enums.Result;
import io.github.helloandrewyan.potions.resource.Recipe;
import io.github.helloandrewyan.potions.step.Step;

public class WandStep extends Step {

		private final String spell;

		public WandStep(String spell) {
				super();
				this.spell = spell;
		}

		public String getSpell() {
				return spell;
		}

		@Override
		public void executeStep(Cauldron cauldron) {
				Recipe recipe = cauldron.getRecipe();
				if (recipe == null) return;
				recipe.handleResult(recipe.getCurrentStep().getResult(this));
		}

		@Override
		public Result getResult(Step step) {
				if (!(step instanceof WandStep wandStep)) return Result.FAILED;
				if (!wandStep.getSpell().equalsIgnoreCase(spell)) return Result.FAILED;

				return Result.COMPLETE;
		}

		@Override
		public Step copy() {
				return new WandStep(spell);
		}

		@Override
		public String getStepString() {
				return "Cast " + spell.toUpperCase();
		}
}

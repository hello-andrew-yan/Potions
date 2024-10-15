package io.github.helloandrewyan.potions.step;

import io.github.helloandrewyan.potions.Potions;
import io.github.helloandrewyan.potions.enums.Direction;
import io.github.helloandrewyan.potions.enums.Heat;
import io.github.helloandrewyan.potions.step.type.HeatStep;
import io.github.helloandrewyan.potions.step.type.IngredientStep;
import io.github.helloandrewyan.potions.step.type.StirStep;
import io.github.helloandrewyan.potions.step.type.WandStep;
import org.json.JSONObject;

/**
 * The {@code StepFactory} class is a factory for creating {@link Step} instances
 * based on the provided JSON representation of a step.
 * <p>
 * It reads the step type from a JSON object and constructs the appropriate
 * {@link Step} subclass (e.g., {@link IngredientStep}, {@link HeatStep},
 * {@link StirStep}, or {@link WandStep}).
 * </p>
 */
public class StepFactory {

		/**
		 * Creates a {@link Step} instance based on the given JSON object.
		 * <p>
		 * The JSON object must include a "type" field that specifies the type of step.
		 * Depending on the type, it constructs and returns an instance of the corresponding
		 * {@link Step} subclass. If the type is not recognized, the method returns {@code null}.
		 * </p>
		 *
		 * @param step A {@link JSONObject} representing the step. It must include the "type" field,
		 *             and additional fields specific to each step type (e.g., "ingredient", "count",
		 *             "heat", "direction", "rotations", or "spell").
		 * @return 		 A {@link Step} instance corresponding to the type specified in the JSON object;
				 *         {@code null} if the type is not recognized.
		 * @throws 		 IllegalArgumentException if the JSON object is missing required fields or contains
		 *    				 invalid values.
		 */
		public static Step getStep(JSONObject step) {
				// Validate and parse the step type from the JSON object.
				String type = step.getString("type").toLowerCase();

				return switch (type) {
						case "ingredient_step" -> new IngredientStep(
										Potions.getIngredients().getIngredient(step.getString("ingredient")),
										step.getInt("count")
						);
						case "heat_step" -> new HeatStep(step.getEnum(Heat.class, "heat"));
						case "stir_step" -> new StirStep(
										step.getEnum(Direction.class, "direction"),
										step.getInt("rotations")
						);
						case "wand_step" -> new WandStep(step.getString("spell"));
						default -> null;
				};
		}
}

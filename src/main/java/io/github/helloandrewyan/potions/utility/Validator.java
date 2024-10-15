package io.github.helloandrewyan.potions.utility;

import com.networknt.schema.*;
import io.github.helloandrewyan.potions.Potions;

import java.util.Set;

/**
 * The {@code Validator} class provides utility methods for validating JSON data
 * against a JSON schema.
 * <p>
 * It uses the NetworkNT JSON Schema Validator library to perform the validation.
 * </p>
 */
public class Validator {

		/**
		 * Validates a JSON string against a specified JSON schema.
		 * <p>
		 * This method uses the 2020-12 version of the JSON Schema specification if the
		 * schema version is not specified. Any validation messages are logged using
		 * {@code Potions.log}.
		 * </p>
		 *
		 * @param objectData	The JSON string to be validated
		 * @param schemaPath	The path to the JSON schema used for validation
		 * @return 					 	{@code true} if the JSON string is valid, according to the schema;
		 * 				 					 	{@code false} otherwise
		 */
		public static boolean isValid(String objectData, String schemaPath) {
				// Uses 2020-12 if the $schema isn't specified.
				JsonSchemaFactory factory = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V202012);
				JsonSchema schema = factory.getSchema(schemaPath, InputFormat.JSON);

				Set<ValidationMessage> assertions = schema.validate(objectData, InputFormat.JSON);
				if (!assertions.isEmpty()) Potions.warn("\tFailed to validate resource.");
				assertions.forEach(action -> Potions.warn("\t" + action.getError()));

				return assertions.isEmpty();
		}
}

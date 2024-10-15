package io.github.helloandrewyan.potions.manager;

import io.github.helloandrewyan.potions.Potions;
import io.github.helloandrewyan.potions.utility.Validator;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Set;

/**
 * The {@code Manager} class serves as an abstract base class for managing resources
 * that are validated against a JSON schema. It provides functionality for loading
 * and validating resources from files in a specified data directory.
 *
 * @param <T> the type of resources managed by this class
 */
public abstract class Manager<T> {

		private final String dataDirectory;
		private final String schemaPath;
		protected final HashMap<String, T> resources = new HashMap<>();

		/**
		 * Constructs a {@code Manager} with the specified data directory and schema path.
		 * <p>
		 * This constructor initializes the manager and reloads resources from the
		 * specified directory.
		 * </p>
		 *
		 * @param dataDirectory the directory where resource files are located
		 * @param schemaPath    the path to the JSON schema used for validation
		 */
		protected Manager(String dataDirectory, String schemaPath) {
				this.dataDirectory = dataDirectory;
				this.schemaPath = schemaPath;
				reloadResources();
		}

		/**
		 * Abstract method to add a resource to the manager.
		 * <p>
		 * This method must be implemented by subclasses to define how resources
		 * should be processed and added.
		 * </p>
		 *
		 * @param resource the JSON representation of the resource to be added
		 */
		protected abstract void addResource(JSONObject resource);

		/**
		 * Retrieves the JSON schema as a {@code String} from the resources directory.
		 * <p>
		 * This method attempts to read the schema file specified by {@code schemaPath}
		 * and return its contents as a {@code String}. If the schema cannot be retrieved
		 * or read, {@code null} is returned.
		 * </p>
		 *
		 * @return the JSON schema as a {@code String}, or {@code null} if an error occurs
		 */
		private String getSchema() {
				InputStream schema = Potions.getInstance().getResource(schemaPath);
				if (schema == null) {
						Potions.warn("Failed to retrieve schema from resources directory.");
						return null;
				}
				try {
						return new String(schema.readAllBytes(), StandardCharsets.UTF_8);
				} catch (IOException e) {
						Potions.warn("Failed to read schema data.");
						return null;
				}
		}

		/**
		 * Retrieves an array of files from the data directory.
		 *
		 * @return an array of {@code File} objects representing the files in the data directory,
		 *         or {@code null} if the directory cannot be created
		 */
		private File[] getFiles() {
				File directory = new File(Potions.getInstance().getDataFolder(), dataDirectory);
				if (directory.mkdirs()) Potions.info("Created \"" + dataDirectory + "\" folder.");
				return directory.listFiles();
		}

		/**
		 * Reloads resources from files in the data directory and validates them
		 * against the JSON schema.
		 * <p>
		 * This method iterates over each file in the data directory, reads its content
		 * as a {@code String}, and validates it using the JSON schema. Valid resources
		 * are added via the {@code addResource} method. Any validation or file reading
		 * errors are logged.
		 * </p>
		 */
		public void reloadResources() {
				resources.clear();
				for (File file : getFiles()) {
						String name = file.getName();
						try {
								Potions.info("Reading \"" + name + "\".");
								String resource = Files.readString(Path.of(file.getPath()));
								if (Validator.isValid(resource, getSchema())) addResource(new JSONObject(resource));
						} catch (Exception e) {
								Potions.warn("Failed to read file: \"" + name + "\". " + e.getMessage());
						}
				}
				Potions.info("Loaded "
								+ resources.size() + "/"
								+ getFiles().length + " resources in \""
								+ dataDirectory + "\"."
				);
		}

		public Set<String> getResourceNames() {
				return resources.keySet();
		}
}

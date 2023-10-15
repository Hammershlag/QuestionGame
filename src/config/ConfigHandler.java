package config;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * The `ConfigHandler` class is responsible for managing configuration properties loaded from a file.
 *
 * @author Tomasz Zbroszczyk
 * @version 1.0
 */
public class ConfigHandler {
    /**
     * The map of configuration properties.
     */
    private final Map<String, String> properties;
    /**
     * The class for which the configuration properties are intended.
     */
    private final Class<?> clazz;

    /**
     * Constructs a new `ConfigHandler` instance and loads configuration properties from a specified file.
     *
     * @param configFile The path to the configuration file.
     * @param clazz      The class for which the configuration properties are intended.
     */
    public ConfigHandler(String configFile, Class<?> clazz) {
        properties = new HashMap<>();
        this.clazz = clazz;
        loadConfig(configFile);
    }

    /**
     * Loads configuration properties from the specified configuration file.
     *
     * @param configFile The path to the configuration file to be loaded.
     */
    private void loadConfig(String configFile) {
        try (BufferedReader br = new BufferedReader(new FileReader(configFile))) {
            String line;
            String section = null;
            while ((line = br.readLine()) != null) {
                line = line.trim();

                // Remove comments (lines starting with #)
                int commentIndex = line.indexOf('#');
                if (commentIndex != -1) {
                    line = line.substring(0, commentIndex).trim();
                }

                if (line.isEmpty()) {
                    continue; // Skip empty lines
                }

                if (line.endsWith(":")) {
                    section = line.substring(0, line.length() - 1).trim();
                } else if (section != null) {
                    String[] parts = line.split("=");
                    if (parts.length == 2) {
                        String key = section + "." + parts[0].trim();
                        String value = parts[1].trim();
                        properties.put(key, value);

                        // If the key starts with "all.", duplicate it for "server." and "client."
                        if (key.startsWith("all.")) {
                            String serverKey = "server." + key.substring(4);
                            String clientKey = "client." + key.substring(4);
                            properties.put(serverKey, value);
                            properties.put(clientKey, value);
                            properties.remove("all." + key.substring(4));
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Get an integer configuration value for the specified key.
     *
     * @param key The key for the configuration property.
     * @return The integer value associated with the key.
     */
    public int getInt(String key) {
        return Integer.parseInt(properties.get(clazz.getSimpleName().toLowerCase() + "." + key));
    }

    /**
     * Get a string configuration value for the specified key.
     *
     * @param key The key for the configuration property.
     * @return The string value associated with the key.
     */
    public String getString(String key) {
        return properties.get(clazz.getSimpleName().toLowerCase() + "." + key);
    }

    /**
     * Get a boolean configuration value for the specified key.
     *
     * @param key The key for the configuration property.
     * @return The boolean value associated with the key.
     */
    public boolean getBoolean(String key) {
        return Boolean.parseBoolean(properties.get(clazz.getSimpleName().toLowerCase() + "." + key));
    }

    /**
     * Get a double configuration value for the specified key.
     *
     * @param key The key for the configuration property.
     * @return The double value associated with the key.
     */
    public double getDouble(String key) {
        return Double.parseDouble(properties.get(clazz.getSimpleName().toLowerCase() + "." + key));
    }

    /**
     * Override a configuration property with a new value.
     *
     * @param key   The key for the configuration property.
     * @param value The new value to set.
     */
    public void overrideProperty(String key, String value) {
        properties.put(clazz.getSimpleName().toLowerCase() + "." + key, value);
    }
}

package config;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ConfigHandler {
    private Map<String, String> properties;

    private Class<?> clazz;

    public ConfigHandler(String configFile, Class<?> clazz) {
        properties = new HashMap<>();
        this.clazz = clazz;
        loadConfig(configFile);
    }

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

    public int getInt(String key) {
        return Integer.parseInt(properties.get(clazz.getSimpleName().toLowerCase() + "." + key));
    }

    public String getString(String key) {
        return properties.get(clazz.getSimpleName().toLowerCase() + "." + key);
    }

    public boolean getBoolean(String key) {
        return Boolean.parseBoolean(properties.get(clazz.getSimpleName().toLowerCase() + "." + key));
    }

    public double getDouble(String key) {
        return Double.parseDouble(properties.get(clazz.getSimpleName().toLowerCase() + "." + key));
    }

    public void overridePropertiy(String key, String value) {
        properties.put(clazz.getSimpleName().toLowerCase() + "." + key, value);
    }

    public void overrideProperties(String[][] args) {
        for(String[] arg : args) {
            overridePropertiy(arg[0], arg[1]);
        }
    }
}


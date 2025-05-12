package utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Utility class for managing configuration properties.
 */
public class ConfigManager {
    private static Properties properties;
    private static final String CONFIG_FILE = "config/config.properties";

    static {
        properties = new Properties();
        try (InputStream input = ConfigManager.class
                .getClassLoader()
                .getResourceAsStream(CONFIG_FILE)) {
            if (input != null) {
                properties.load(input);
            } else {
                System.err.println("Unable to find " + CONFIG_FILE);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static String getBaseUrl() {
        return properties.getProperty("api.base.url");
    }

    public static int getConnectTimeout() {
        return Integer.parseInt(properties.getProperty("api.timeout.connect", "10000"));
    }
    
    public static int getReadTimeout() {
        return Integer.parseInt(properties.getProperty("api.timeout.read", "30000"));
    }
    
    public static String getUsername() {
        return properties.getProperty("api.username");
    }
    
    public static String getPassword() {
        return properties.getProperty("api.password");
    }
    
    public static int getMaxRetries() {
        return Integer.parseInt(properties.getProperty("api.retry.max", "3"));
    }
    
    public static int getRetryDelay() {
        return Integer.parseInt(properties.getProperty("api.retry.delay", "2000"));
    }
}

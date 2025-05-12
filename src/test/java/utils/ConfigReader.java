package utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Utility class for reading configuration properties.
 */
public class ConfigReader {
    private static final Properties properties = new Properties();
    private static final String CONFIG_FILE_PATH = "src/test/resources/config/config.properties";
    private static boolean isInitialized = false;

    /**
     * Initializes the configuration properties.
     */
    public static void initialize() {
        if (!isInitialized) {
            try (FileInputStream fis = new FileInputStream(CONFIG_FILE_PATH)) {
                properties.load(fis);
                isInitialized = true;
            } catch (IOException e) {
                throw new RuntimeException("Failed to load configuration properties: " + e.getMessage(), e);
            }
        }
    }

    /**
     * Gets a property value by key.
     *
     * @param key the property key
     * @return the property value
     */
    public static String getProperty(String key) {
        if (!isInitialized) {
            initialize();
        }
        return properties.getProperty(key);
    }

    /**
     * Gets a property value by key with a default value.
     *
     * @param key          the property key
     * @param defaultValue the default value
     * @return the property value or default value if not found
     */
    public static String getProperty(String key, String defaultValue) {
        if (!isInitialized) {
            initialize();
        }
        return properties.getProperty(key, defaultValue);
    }

    /**
     * Gets the base URL for the API.
     *
     * @return the base URL
     */
    public static String getBaseUrl() {
        return getProperty("api.base.url");
    }

    /**
     * Gets the API username.
     *
     * @return the username
     */
    public static String getUsername() {
        return getProperty("api.username");
    }

    /**
     * Gets the API password.
     *
     * @return the password
     */
    public static String getPassword() {
        return getProperty("api.password");
    }

    /**
     * Gets the connection timeout in milliseconds.
     *
     * @return the connection timeout
     */
    public static int getConnectTimeout() {
        return Integer.parseInt(getProperty("api.timeout.connect", "10000"));
    }

    /**
     * Gets the read timeout in milliseconds.
     *
     * @return the read timeout
     */
    public static int getReadTimeout() {
        return Integer.parseInt(getProperty("api.timeout.read", "30000"));
    }

    /**
     * Gets the maximum number of retries.
     *
     * @return the maximum number of retries
     */
    public static int getMaxRetries() {
        return Integer.parseInt(getProperty("api.retry.max", "3"));
    }

    /**
     * Gets the retry delay in milliseconds.
     *
     * @return the retry delay
     */
    public static int getRetryDelay() {
        return Integer.parseInt(getProperty("api.retry.delay", "2000"));
    }
}
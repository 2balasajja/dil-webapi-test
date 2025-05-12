package utils;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import static io.restassured.RestAssured.*;

/**
 * Helper class for authentication-related operations.
 */
public class AuthHelper {
    private static final String BASE_URL = ConfigManager.getBaseUrl();

    /**
     * Gets an authentication token for the specified credentials.
     *
     * @param username the username
     * @param password the password
     * @return the authentication token
     */
    public static String getAuthToken(String username, String password) {
        Response response = RestAssured.given()
                .contentType("application/json")
                .body(String.format("{ \"username\": \"%s\", \"password\": \"%s\" }", username, password))
                .post(BASE_URL + "/auth/login");

        return response.jsonPath().getString("token");
    }
    
    /**
     * Gets an authentication token using the default credentials from configuration.
     *
     * @return the authentication token
     */
    public static String getDefaultAuthToken() {
        String username = ConfigManager.getUsername();
        String password = ConfigManager.getPassword();
        return getAuthToken(username, password);
    }
}

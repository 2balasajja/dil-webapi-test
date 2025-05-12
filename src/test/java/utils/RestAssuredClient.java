package utils;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.dwp.model.AuthRequest;
import org.dwp.model.AuthResponse;

/**
 * Utility class for making REST API calls using RestAssured.
 */
public class RestAssuredClient {
    private static String authToken;
    private static RequestSpecification requestSpec;

    /**
     * Initializes the REST client.
     */
    public static void initialize() {
        RestAssured.baseURI = ConfigManager.getBaseUrl();
        
        requestSpec = new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                .addFilter(new RequestLoggingFilter())
                .addFilter(new ResponseLoggingFilter())
                .build();
    }

    /**
     * Authenticates with the API and stores the token.
     *
     * @param username the username
     * @param password the password
     * @return the authentication response
     */
    public static AuthResponse authenticate(String username, String password) {
        AuthRequest authRequest = new AuthRequest(username, password);
        
        Response response = RestAssured.given(requestSpec)
                .body(authRequest)
                .when()
                .post("/auth/login");
        
        AuthResponse authResponse = response.as(AuthResponse.class);
        
        if (response.getStatusCode() == 200 && authResponse.getToken() != null) {
            authToken = authResponse.getToken();
        }
        
        return authResponse;
    }

    /**
     * Gets the request specification with authentication token.
     *
     * @return the authenticated request specification
     */
    public static RequestSpecification getAuthenticatedRequestSpec() {
        if (authToken == null) {
            throw new IllegalStateException("Authentication token is not set. Please authenticate first.");
        }
        
        return RestAssured.given(requestSpec)
                .header("Authorization", "Bearer " + authToken);
    }

    /**
     * Gets the request specification without authentication token.
     *
     * @return the unauthenticated request specification
     */
    public static RequestSpecification getUnauthenticatedRequestSpec() {
        return RestAssured.given(requestSpec);
    }

    /**
     * Clears the authentication token.
     */
    public static void clearAuthToken() {
        authToken = null;
    }

    /**
     * Checks if the client is authenticated.
     *
     * @return true if authenticated, false otherwise
     */
    public static boolean isAuthenticated() {
        return authToken != null;
    }
}
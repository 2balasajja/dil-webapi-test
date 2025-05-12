package stepDefinitions;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import org.dwp.model.AuthResponse;
import org.junit.jupiter.api.Assertions;
import utils.ConfigManager;
import utils.RestAssuredClient;
import utils.TestContext;

/**
 * Step definitions for authentication scenarios.
 */
public class AuthenticationSteps extends BaseSteps {

    public AuthenticationSteps(TestContext testContext) {
        super(testContext);
    }

    @Given("the API base URL is set")
    public void theAPIBaseURLIsSet() {
        Assertions.assertNotNull(ConfigManager.getBaseUrl(), "API base URL should not be null");
    }

    @Given("I am authenticated with valid credentials")
    public void iAmAuthenticatedWithValidCredentials() {
        String username = ConfigManager.getUsername();
        String password = ConfigManager.getPassword();
        
        AuthResponse authResponse = RestAssuredClient.authenticate(username, password);
        
        Assertions.assertNotNull(authResponse.getToken(), "Authentication token should not be null");
        Assertions.assertTrue(RestAssuredClient.isAuthenticated(), "Client should be authenticated");
    }

    @Given("I am authenticated with username {string} and password {string}")
    public void iAmAuthenticatedWithUsernameAndPassword(String username, String password) {
        AuthResponse authResponse = RestAssuredClient.authenticate(username, password);
        
        Assertions.assertNotNull(authResponse.getToken(), "Authentication token should not be null");
        Assertions.assertTrue(RestAssuredClient.isAuthenticated(), "Client should be authenticated");
    }

    @Given("I am not authenticated")
    public void iAmNotAuthenticated() {
        RestAssuredClient.clearAuthToken();
        Assertions.assertFalse(RestAssuredClient.isAuthenticated(), "Client should not be authenticated");
    }

    @When("I authenticate with username {string} and password {string}")
    public void iAuthenticateWithUsernameAndPassword(String username, String password) {
        Response response = RestAssuredClient.getUnauthenticatedRequestSpec()
                .body("{ \"username\": \"" + username + "\", \"password\": \"" + password + "\" }")
                .when()
                .post("/auth/login");
        
        testContext.setLastResponse(response);
        
        if (response.getStatusCode() == 200) {
            AuthResponse authResponse = response.as(AuthResponse.class);
            if (authResponse.getToken() != null) {
                testContext.setContext("authToken", authResponse.getToken());
            }
        }
    }

    @Then("the authentication response status code should be {int}")
    public void theAuthenticationResponseStatusCodeShouldBe(int expectedStatusCode) {
        Response response = testContext.getLastResponse();
        Assertions.assertEquals(expectedStatusCode, response.getStatusCode(), 
                "Status code should be " + expectedStatusCode);
    }

    @Then("the authentication response should contain a valid token")
    public void theAuthenticationResponseShouldContainAValidToken() {
        Response response = testContext.getLastResponse();
        AuthResponse authResponse = response.as(AuthResponse.class);
        
        Assertions.assertNotNull(authResponse.getToken(), "Authentication token should not be null");
        Assertions.assertFalse(authResponse.getToken().isEmpty(), "Authentication token should not be empty");
    }

    @Then("the authentication response should contain user details")
    public void theAuthenticationResponseShouldContainUserDetails() {
        Response response = testContext.getLastResponse();
        AuthResponse authResponse = response.as(AuthResponse.class);
        
        Assertions.assertNotNull(authResponse.getUserId(), "User ID should not be null");
    }

    @Then("the authentication response should contain an error message")
    public void theAuthenticationResponseShouldContainAnErrorMessage() {
        Response response = testContext.getLastResponse();
        String responseBody = response.getBody().asString();
        
        Assertions.assertTrue(responseBody.contains("error") || responseBody.contains("message"), 
                "Response should contain error information");
    }
}
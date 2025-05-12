package stepDefinitions;

import utils.AuthHelper;
import utils.ConfigManager;
import io.restassured.response.Response;
import io.cucumber.java.en.*;
import static io.restassured.RestAssured.*;
import static org.junit.Assert.*;

public class InventorySteps {

    private Response response;
    private String token;
    private String username;
    private String password;
    private final String BASE_URL = ConfigManager.getBaseUrl();

    @Given("I authenticate using {string} and {string}")
    public void authenticateUser(String username, String password) {
        this.username = username;
        this.password = password;
        token = AuthHelper.getAuthToken(username, password);
        assertNotNull(token);
    }

    @When("I send a request to add a product with name {string} and stock {int}")
    public void addProduct(String name, int stock) {
        response = given()
                .header("Authorization", "Bearer " + token)
                .contentType("application/json")
                .body("{ \"name\": \"" + name + "\", \"stock\": " + stock + " }")
                .post(BASE_URL + "/api/products");

        assertNotNull(response);
    }

    @Then("the response status should be {int}")
    public void verifyResponseStatus(int expectedStatus) {
        assertEquals(expectedStatus, response.statusCode());
    }
}

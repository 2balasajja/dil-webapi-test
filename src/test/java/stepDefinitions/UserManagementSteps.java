package stepDefinitions;

import utils.AuthHelper;
import utils.ConfigReader;
import io.restassured.response.Response;
import io.cucumber.java.en.*;
import io.cucumber.datatable.DataTable;
import static io.restassured.RestAssured.*;
import static org.junit.Assert.*;
import java.util.List;
import java.util.Map;

public class UserManagementSteps {

    private Response response;
    private String token;
    private String username;
    private final String BASE_URL = ConfigReader.getProperty("api.base.url");

    @When("I send a request to create a user with:")
    public void createUser(DataTable dataTable) {
        List<Map<String, String>> users = dataTable.asMaps(String.class, String.class);
        Map<String, String> user = users.get(0);
        
        username = user.get("username");
        String email = user.get("email");
        String role = user.get("role");
        
        response = given()
                .header("Authorization", "Bearer " + token)
                .contentType("application/json")
                .body(String.format("{ \"username\": \"%s\", \"email\": \"%s\", \"role\": \"%s\", \"password\": \"password123\" }", 
                        username, email, role))
                .post(BASE_URL + "/api/users");
        
        assertNotNull(response);
    }

    @Then("the user should be created successfully")
    public void verifyUserCreated() {
        assertEquals(username, response.jsonPath().getString("username"));
    }

    @Given("a user with username {string} exists")
    public void userExists(String username) {
        this.username = username;
        
        // First ensure the user exists by trying to get it
        response = given()
                .header("Authorization", "Bearer " + token)
                .get(BASE_URL + "/api/users/" + username);
        
        // If user doesn't exist, create it
        if (response.statusCode() != 200) {
            response = given()
                    .header("Authorization", "Bearer " + token)
                    .contentType("application/json")
                    .body(String.format("{ \"username\": \"%s\", \"email\": \"%s@example.com\", \"role\": \"user\", \"password\": \"password123\" }", 
                            username, username))
                    .post(BASE_URL + "/api/users");
        }
    }

    @Then("the response should contain user details")
    public void verifyUserDetails() {
        String responseUsername = response.jsonPath().getString("username");
        String email = response.jsonPath().getString("email");
        String role = response.jsonPath().getString("role");
        
        assertNotNull(responseUsername);
        assertNotNull(email);
        assertNotNull(role);
        assertEquals(username, responseUsername);
    }

    @When("I send a PUT request to update user {string} with role {string}")
    public void updateUserRole(String username, String role) {
        response = given()
                .header("Authorization", "Bearer " + token)
                .contentType("application/json")
                .body(String.format("{ \"role\": \"%s\" }", role))
                .put(BASE_URL + "/api/users/" + username);
        
        assertNotNull(response);
    }

    @Then("the user role should be updated to {string}")
    public void verifyUpdatedRole(String expectedRole) {
        String actualRole = response.jsonPath().getString("role");
        assertEquals(expectedRole, actualRole);
    }

    @Then("the user should no longer exist")
    public void verifyUserDeleted() {
        // Verify by trying to get the user
        Response getResponse = given()
                .header("Authorization", "Bearer " + token)
                .get(BASE_URL + "/api/users/" + username);
        
        assertEquals(404, getResponse.statusCode());
    }
}
package stepDefinitions;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import utils.ConfigManager;
import utils.RestAssuredClient;
import utils.TestContext;

/**
 * Hooks for Cucumber scenarios.
 */
public class Hooks {
    private final TestContext testContext;

    public Hooks(TestContext testContext) {
        this.testContext = testContext;
    }

    @Before
    public void setUp(Scenario scenario) {
        // Initialize REST client (ConfigManager is initialized statically)
        RestAssuredClient.initialize();
        
        // Log scenario information
        System.out.println("Starting scenario: " + scenario.getName());
        System.out.println("Tags: " + scenario.getSourceTagNames());
    }

    @After
    public void tearDown(Scenario scenario) {
        // Log scenario result
        System.out.println("Scenario " + scenario.getName() + " finished with status: " + scenario.getStatus());
        
        // Clean up resources
        RestAssuredClient.clearAuthToken();
        testContext.clearContext();
        
        // Attach response to report if scenario failed
        if (scenario.isFailed() && testContext.getLastResponse() != null) {
            String responseBody = testContext.getLastResponse().getBody().asString();
            scenario.attach(responseBody, "application/json", "API Response");
        }
    }
}
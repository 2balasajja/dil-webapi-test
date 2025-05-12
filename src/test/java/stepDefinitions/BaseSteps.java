package stepDefinitions;

import io.cucumber.java.Before;
import utils.ConfigManager;
import utils.RestAssuredClient;
import utils.TestContext;

/**
 * Base class for step definitions with common functionality.
 */
public class BaseSteps {
    protected final TestContext testContext;

    public BaseSteps(TestContext testContext) {
        this.testContext = testContext;
    }

    @Before
    public void setup() {
        // ConfigManager is initialized statically, no need to call initialize()
        RestAssuredClient.initialize();
    }
}
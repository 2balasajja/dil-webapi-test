package stepDefinitions;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import utils.ConfigReader;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class hmrcApiTestSteps {
    private Response response;

    @Given("I set up the HMRC API endpoint")
    public void setupApiEndpoint() {

//        RestAssured.baseURI = "https://hmrc.com";
        RestAssured.baseURI = ConfigReader.getEnvProperty("baseUrl");
        System.out.println("Running tests in environment: " + ConfigReader.getProperty("env"));

    }

  }

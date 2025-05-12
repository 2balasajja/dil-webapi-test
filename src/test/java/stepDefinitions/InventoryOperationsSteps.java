package stepDefinitions;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import org.dwp.model.InventoryRequest;
import org.dwp.model.InventoryResponse;
import org.dwp.model.Product;
import org.junit.jupiter.api.Assertions;
import utils.RestAssuredClient;
import utils.TestContext;

import java.util.UUID;

/**
 * Step definitions for inventory operations scenarios.
 */
public class InventoryOperationsSteps extends BaseSteps {

    public InventoryOperationsSteps(TestContext testContext) {
        super(testContext);
    }

    @When("I buy {string} units of the product")
    public void iBuyUnitsOfTheProduct(String quantity) {
        String productId = testContext.getCurrentProductId();
        int quantityValue = Integer.parseInt(quantity);
        
        InventoryRequest request = InventoryRequest.builder()
                .productId(productId)
                .quantity(quantityValue)
                .build();
        
        Response response = RestAssuredClient.getAuthenticatedRequestSpec()
                .body(request)
                .when()
                .post("/inventory/buy");
        
        testContext.setLastResponse(response);
        
        if (response.getStatusCode() == 200) {
            InventoryResponse inventoryResponse = response.as(InventoryResponse.class);
            if (inventoryResponse.getProduct() != null) {
                testContext.setCurrentProduct(inventoryResponse.getProduct());
            }
        }
    }

    @When("I sell {string} units of the product")
    public void iSellUnitsOfTheProduct(String quantity) {
        String productId = testContext.getCurrentProductId();
        int quantityValue = Integer.parseInt(quantity);
        
        InventoryRequest request = InventoryRequest.builder()
                .productId(productId)
                .quantity(quantityValue)
                .build();
        
        Response response = RestAssuredClient.getAuthenticatedRequestSpec()
                .body(request)
                .when()
                .post("/inventory/sell");
        
        testContext.setLastResponse(response);
        
        if (response.getStatusCode() == 200) {
            InventoryResponse inventoryResponse = response.as(InventoryResponse.class);
            if (inventoryResponse.getProduct() != null) {
                testContext.setCurrentProduct(inventoryResponse.getProduct());
            }
        }
    }

    @When("I request the stock level for the product")
    public void iRequestTheStockLevelForTheProduct() {
        String productId = testContext.getCurrentProductId();
        
        Response response = RestAssuredClient.getAuthenticatedRequestSpec()
                .when()
                .get("/products/" + productId);
        
        testContext.setLastResponse(response);
        
        if (response.getStatusCode() == 200) {
            Product product = response.as(Product.class);
            testContext.setCurrentProduct(product);
        }
    }

    @When("I attempt to buy {string} units of the product")
    public void iAttemptToBuyUnitsOfTheProduct(String quantity) {
        String productId = testContext.getCurrentProductId();
        int quantityValue = Integer.parseInt(quantity);
        
        InventoryRequest request = InventoryRequest.builder()
                .productId(productId)
                .quantity(quantityValue)
                .build();
        
        Response response = RestAssuredClient.getAuthenticatedRequestSpec()
                .body(request)
                .when()
                .post("/inventory/buy");
        
        testContext.setLastResponse(response);
    }

    @When("I attempt to buy {string} units of a non-existent product")
    public void iAttemptToBuyUnitsOfANonExistentProduct(String quantity) {
        String invalidId = "invalid-id-" + UUID.randomUUID().toString();
        int quantityValue = Integer.parseInt(quantity);
        
        InventoryRequest request = InventoryRequest.builder()
                .productId(invalidId)
                .quantity(quantityValue)
                .build();
        
        Response response = RestAssuredClient.getAuthenticatedRequestSpec()
                .body(request)
                .when()
                .post("/inventory/buy");
        
        testContext.setLastResponse(response);
    }

    @When("I attempt to sell {string} units of a non-existent product")
    public void iAttemptToSellUnitsOfANonExistentProduct(String quantity) {
        String invalidId = "invalid-id-" + UUID.randomUUID().toString();
        int quantityValue = Integer.parseInt(quantity);
        
        InventoryRequest request = InventoryRequest.builder()
                .productId(invalidId)
                .quantity(quantityValue)
                .build();
        
        Response response = RestAssuredClient.getAuthenticatedRequestSpec()
                .body(request)
                .when()
                .post("/inventory/sell");
        
        testContext.setLastResponse(response);
    }

    @Then("the purchase should be successful")
    public void thePurchaseShouldBeSuccessful() {
        Response response = testContext.getLastResponse();
        Assertions.assertEquals(200, response.getStatusCode(), "Status code should be 200");
        
        InventoryResponse inventoryResponse = response.as(InventoryResponse.class);
        Assertions.assertTrue(inventoryResponse.isSuccess(), "Operation should be successful");
    }

    @Then("the sale should be successful")
    public void theSaleShouldBeSuccessful() {
        Response response = testContext.getLastResponse();
        Assertions.assertEquals(200, response.getStatusCode(), "Status code should be 200");
        
        InventoryResponse inventoryResponse = response.as(InventoryResponse.class);
        Assertions.assertTrue(inventoryResponse.isSuccess(), "Operation should be successful");
    }

    @Then("the product stock should be decreased by {string} units")
    public void theProductStockShouldBeDecreasedByUnits(String quantity) {
        Product originalProduct = testContext.getCurrentProduct();
        int originalQuantity = originalProduct.getQuantity();
        int decreaseAmount = Integer.parseInt(quantity);
        
        // Get the latest product data
        String productId = testContext.getCurrentProductId();
        Response response = RestAssuredClient.getAuthenticatedRequestSpec()
                .when()
                .get("/products/" + productId);
        
        Assertions.assertEquals(200, response.getStatusCode(), "Status code should be 200");
        
        Product updatedProduct = response.as(Product.class);
        int newQuantity = updatedProduct.getQuantity();
        
        Assertions.assertEquals(originalQuantity - decreaseAmount, newQuantity, 
                "Product quantity should be decreased by " + quantity + " units");
    }

    @Then("the product stock should be increased by {string} units")
    public void theProductStockShouldBeIncreasedByUnits(String quantity) {
        Product originalProduct = testContext.getCurrentProduct();
        int originalQuantity = originalProduct.getQuantity();
        int increaseAmount = Integer.parseInt(quantity);
        
        // Get the latest product data
        String productId = testContext.getCurrentProductId();
        Response response = RestAssuredClient.getAuthenticatedRequestSpec()
                .when()
                .get("/products/" + productId);
        
        Assertions.assertEquals(200, response.getStatusCode(), "Status code should be 200");
        
        Product updatedProduct = response.as(Product.class);
        int newQuantity = updatedProduct.getQuantity();
        
        Assertions.assertEquals(originalQuantity + increaseAmount, newQuantity, 
                "Product quantity should be increased by " + quantity + " units");
    }

    @Then("I should receive a purchase confirmation")
    public void iShouldReceiveAPurchaseConfirmation() {
        Response response = testContext.getLastResponse();
        InventoryResponse inventoryResponse = response.as(InventoryResponse.class);
        
        Assertions.assertNotNull(inventoryResponse.getMessage(), "Confirmation message should not be null");
        Assertions.assertTrue(inventoryResponse.getMessage().toLowerCase().contains("purchase") || 
                inventoryResponse.getMessage().toLowerCase().contains("buy"), 
                "Message should confirm the purchase");
    }

    @Then("I should receive a sale confirmation")
    public void iShouldReceiveASaleConfirmation() {
        Response response = testContext.getLastResponse();
        InventoryResponse inventoryResponse = response.as(InventoryResponse.class);
        
        Assertions.assertNotNull(inventoryResponse.getMessage(), "Confirmation message should not be null");
        Assertions.assertTrue(inventoryResponse.getMessage().toLowerCase().contains("sale") || 
                inventoryResponse.getMessage().toLowerCase().contains("sell"), 
                "Message should confirm the sale");
    }

    @Then("I should receive the correct stock information")
    public void iShouldReceiveTheCorrectStockInformation() {
        Response response = testContext.getLastResponse();
        Assertions.assertEquals(200, response.getStatusCode(), "Status code should be 200");
        
        Product product = response.as(Product.class);
        Assertions.assertNotNull(product.getQuantity(), "Product quantity should not be null");
    }

    @Then("I should receive an insufficient stock error message")
    public void iShouldReceiveAnInsufficientStockErrorMessage() {
        Response response = testContext.getLastResponse();
        String responseBody = response.getBody().asString();
        
        Assertions.assertTrue(responseBody.toLowerCase().contains("insufficient") || 
                responseBody.toLowerCase().contains("stock") || 
                responseBody.toLowerCase().contains("quantity"), 
                "Response should mention insufficient stock");
    }

    @Then("the product stock should remain unchanged")
    public void theProductStockShouldRemainUnchanged() {
        Product originalProduct = testContext.getCurrentProduct();
        int originalQuantity = originalProduct.getQuantity();
        
        // Get the latest product data
        String productId = testContext.getCurrentProductId();
        Response response = RestAssuredClient.getAuthenticatedRequestSpec()
                .when()
                .get("/products/" + productId);
        
        Assertions.assertEquals(200, response.getStatusCode(), "Status code should be 200");
        
        Product updatedProduct = response.as(Product.class);
        int newQuantity = updatedProduct.getQuantity();
        
        Assertions.assertEquals(originalQuantity, newQuantity, "Product quantity should remain unchanged");
    }
}
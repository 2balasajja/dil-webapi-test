package stepDefinitions;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import org.dwp.model.Product;
import org.junit.jupiter.api.Assertions;
import utils.RestAssuredClient;
import utils.TestContext;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Step definitions for product management scenarios.
 */
public class ProductManagementSteps extends BaseSteps {

    public ProductManagementSteps(TestContext testContext) {
        super(testContext);
    }

    @When("I add a new product with the following details:")
    public void iAddANewProductWithTheFollowingDetails(DataTable dataTable) {
        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
        Map<String, String> productData = rows.get(0);
        
        Product product = Product.builder()
                .name(productData.get("name"))
                .description(productData.get("description"))
                .price(Double.parseDouble(productData.get("price")))
                .quantity(Integer.parseInt(productData.get("quantity")))
                .build();
        
        Response response = RestAssuredClient.getAuthenticatedRequestSpec()
                .body(product)
                .when()
                .post("/products");
        
        testContext.setLastResponse(response);
        
        if (response.getStatusCode() == 201) {
            Product createdProduct = response.as(Product.class);
            testContext.setCurrentProduct(createdProduct);
            testContext.setCurrentProductId(createdProduct.getId());
        }
    }

    @Then("the product should be added successfully")
    public void theProductShouldBeAddedSuccessfully() {
        Response response = testContext.getLastResponse();
        Assertions.assertEquals(201, response.getStatusCode(), "Status code should be 201");
    }

    @Then("the response should include the product details")
    public void theResponseShouldIncludeTheProductDetails() {
        Response response = testContext.getLastResponse();
        Product product = response.as(Product.class);
        
        Assertions.assertNotNull(product.getId(), "Product ID should not be null");
        Assertions.assertNotNull(product.getName(), "Product name should not be null");
        Assertions.assertNotNull(product.getDescription(), "Product description should not be null");
    }

    @Then("the product should be available in the inventory")
    public void theProductShouldBeAvailableInTheInventory() {
        String productId = testContext.getCurrentProductId();
        
        Response response = RestAssuredClient.getAuthenticatedRequestSpec()
                .when()
                .get("/products/" + productId);
        
        Assertions.assertEquals(200, response.getStatusCode(), "Status code should be 200");
        
        Product product = response.as(Product.class);
        Assertions.assertEquals(productId, product.getId(), "Product ID should match");
    }

    @Given("there is an existing product in the inventory")
    public void thereIsAnExistingProductInTheInventory() {
        // Create a test product
        String uniqueName = "Test Product " + UUID.randomUUID().toString().substring(0, 8);
        
        Product product = Product.builder()
                .name(uniqueName)
                .description("Test product description")
                .price(19.99)
                .quantity(100)
                .build();
        
        Response response = RestAssuredClient.getAuthenticatedRequestSpec()
                .body(product)
                .when()
                .post("/products");
        
        Assertions.assertEquals(201, response.getStatusCode(), "Status code should be 201");
        
        Product createdProduct = response.as(Product.class);
        testContext.setCurrentProduct(createdProduct);
        testContext.setCurrentProductId(createdProduct.getId());
    }

    @Given("there is an existing product in the inventory with sufficient stock")
    public void thereIsAnExistingProductInTheInventoryWithSufficientStock() {
        // Create a test product with sufficient stock
        String uniqueName = "Test Product " + UUID.randomUUID().toString().substring(0, 8);
        
        Product product = Product.builder()
                .name(uniqueName)
                .description("Test product description")
                .price(19.99)
                .quantity(100)
                .build();
        
        Response response = RestAssuredClient.getAuthenticatedRequestSpec()
                .body(product)
                .when()
                .post("/products");
        
        Assertions.assertEquals(201, response.getStatusCode(), "Status code should be 201");
        
        Product createdProduct = response.as(Product.class);
        testContext.setCurrentProduct(createdProduct);
        testContext.setCurrentProductId(createdProduct.getId());
    }

    @Given("there is an existing product in the inventory with {string} units in stock")
    public void thereIsAnExistingProductInTheInventoryWithUnitsInStock(String quantity) {
        // Create a test product with specific quantity
        String uniqueName = "Test Product " + UUID.randomUUID().toString().substring(0, 8);
        int stockQuantity = Integer.parseInt(quantity);
        
        Product product = Product.builder()
                .name(uniqueName)
                .description("Test product description")
                .price(19.99)
                .quantity(stockQuantity)
                .build();
        
        Response response = RestAssuredClient.getAuthenticatedRequestSpec()
                .body(product)
                .when()
                .post("/products");
        
        Assertions.assertEquals(201, response.getStatusCode(), "Status code should be 201");
        
        Product createdProduct = response.as(Product.class);
        testContext.setCurrentProduct(createdProduct);
        testContext.setCurrentProductId(createdProduct.getId());
    }

    @When("I update the product with the following details:")
    public void iUpdateTheProductWithTheFollowingDetails(DataTable dataTable) {
        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
        Map<String, String> productData = rows.get(0);
        String productId = testContext.getCurrentProductId();
        
        Product product = Product.builder()
                .id(productId)
                .name(productData.get("name"))
                .description(productData.get("description"))
                .price(Double.parseDouble(productData.get("price")))
                .quantity(Integer.parseInt(productData.get("quantity")))
                .build();
        
        Response response = RestAssuredClient.getAuthenticatedRequestSpec()
                .body(product)
                .when()
                .put("/products/" + productId);
        
        testContext.setLastResponse(response);
        
        if (response.getStatusCode() == 200) {
            Product updatedProduct = response.as(Product.class);
            testContext.setCurrentProduct(updatedProduct);
        }
    }

    @Then("the product should be updated successfully")
    public void theProductShouldBeUpdatedSuccessfully() {
        Response response = testContext.getLastResponse();
        Assertions.assertEquals(200, response.getStatusCode(), "Status code should be 200");
    }

    @Then("the response should include the updated product details")
    public void theResponseShouldIncludeTheUpdatedProductDetails() {
        Response response = testContext.getLastResponse();
        Product product = response.as(Product.class);
        
        Assertions.assertNotNull(product.getId(), "Product ID should not be null");
        Assertions.assertEquals(testContext.getCurrentProductId(), product.getId(), "Product ID should match");
    }

    @Then("the product details should be updated in the inventory")
    public void theProductDetailsShouldBeUpdatedInTheInventory() {
        String productId = testContext.getCurrentProductId();
        
        Response response = RestAssuredClient.getAuthenticatedRequestSpec()
                .when()
                .get("/products/" + productId);
        
        Assertions.assertEquals(200, response.getStatusCode(), "Status code should be 200");
        
        Product product = response.as(Product.class);
        Product updatedProduct = testContext.getCurrentProduct();
        
        Assertions.assertEquals(updatedProduct.getName(), product.getName(), "Product name should match");
        Assertions.assertEquals(updatedProduct.getDescription(), product.getDescription(), "Product description should match");
        Assertions.assertEquals(updatedProduct.getPrice(), product.getPrice(), 0.001, "Product price should match");
        Assertions.assertEquals(updatedProduct.getQuantity(), product.getQuantity(), "Product quantity should match");
    }

    @When("I delete the product")
    public void iDeleteTheProduct() {
        String productId = testContext.getCurrentProductId();
        
        Response response = RestAssuredClient.getAuthenticatedRequestSpec()
                .when()
                .delete("/products/" + productId);
        
        testContext.setLastResponse(response);
    }

    @Then("the product should be deleted successfully")
    public void theProductShouldBeDeletedSuccessfully() {
        Response response = testContext.getLastResponse();
        Assertions.assertEquals(200, response.getStatusCode(), "Status code should be 200");
    }

    @Then("the product should no longer be available in the inventory")
    public void theProductShouldNoLongerBeAvailableInTheInventory() {
        String productId = testContext.getCurrentProductId();
        
        Response response = RestAssuredClient.getAuthenticatedRequestSpec()
                .when()
                .get("/products/" + productId);
        
        Assertions.assertEquals(404, response.getStatusCode(), "Status code should be 404");
    }

    @When("I attempt to add a new product")
    public void iAttemptToAddANewProduct() {
        Product product = Product.builder()
                .name("Test Product")
                .description("Test product description")
                .price(19.99)
                .quantity(100)
                .build();
        
        Response response = RestAssuredClient.getUnauthenticatedRequestSpec()
                .body(product)
                .when()
                .post("/products");
        
        testContext.setLastResponse(response);
    }

    @When("I attempt to update a product with an invalid ID")
    public void iAttemptToUpdateAProductWithAnInvalidID() {
        String invalidId = "invalid-id-" + UUID.randomUUID().toString();
        
        Product product = Product.builder()
                .id(invalidId)
                .name("Updated Product")
                .description("Updated product description")
                .price(29.99)
                .quantity(150)
                .build();
        
        Response response = RestAssuredClient.getAuthenticatedRequestSpec()
                .body(product)
                .when()
                .put("/products/" + invalidId);
        
        testContext.setLastResponse(response);
    }

    @When("I attempt to delete a product with an invalid ID")
    public void iAttemptToDeleteAProductWithAnInvalidID() {
        String invalidId = "invalid-id-" + UUID.randomUUID().toString();
        
        Response response = RestAssuredClient.getAuthenticatedRequestSpec()
                .when()
                .delete("/products/" + invalidId);
        
        testContext.setLastResponse(response);
    }

    @Then("the operation should fail")
    public void theOperationShouldFail() {
        Response response = testContext.getLastResponse();
        int statusCode = response.getStatusCode();
        
        Assertions.assertTrue(statusCode >= 400, "Status code should be an error code (4xx or 5xx)");
    }

    @Then("I should receive an authentication error")
    public void iShouldReceiveAnAuthenticationError() {
        Response response = testContext.getLastResponse();
        int statusCode = response.getStatusCode();
        
        Assertions.assertTrue(statusCode == 401 || statusCode == 403, 
                "Status code should be 401 (Unauthorized) or 403 (Forbidden)");
    }
}
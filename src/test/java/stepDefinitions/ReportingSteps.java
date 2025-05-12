package stepDefinitions;

import utils.AuthHelper;
import utils.ConfigReader;
import io.restassured.response.Response;
import io.cucumber.java.en.*;
import static io.restassured.RestAssured.*;
import static org.junit.Assert.*;
import java.util.List;

public class ReportingSteps {

    private Response response;
    private String token;
    private final String BASE_URL = ConfigReader.getProperty("api.base.url");

    @When("I request an inventory summary report")
    public void requestInventorySummary() {
        response = given()
                .header("Authorization", "Bearer " + token)
                .get(BASE_URL + "/api/reports/summary");
        
        assertNotNull(response);
    }

    @Then("the report should contain total product count")
    public void verifyTotalProductCount() {
        Integer totalProducts = response.jsonPath().getInt("totalProducts");
        assertNotNull(totalProducts);
        assertTrue(totalProducts >= 0);
    }

    @Then("the report should contain total stock value")
    public void verifyTotalStockValue() {
        Double totalValue = response.jsonPath().getDouble("totalValue");
        assertNotNull(totalValue);
        assertTrue(totalValue >= 0);
    }

    @When("I request a low stock report with threshold {int}")
    public void requestLowStockReport(int threshold) {
        response = given()
                .header("Authorization", "Bearer " + token)
                .queryParam("threshold", threshold)
                .get(BASE_URL + "/api/reports/low-stock");
        
        assertNotNull(response);
    }

    @Then("the report should only include products with stock below {int}")
    public void verifyLowStockProducts(int threshold) {
        List<Integer> stockLevels = response.jsonPath().getList("products.stock");
        
        for (Integer stock : stockLevels) {
            assertTrue(stock < threshold);
        }
    }

    @When("I request a product movement report for the last {int} days")
    public void requestProductMovementReport(int days) {
        response = given()
                .header("Authorization", "Bearer " + token)
                .queryParam("days", days)
                .get(BASE_URL + "/api/reports/movement");
        
        assertNotNull(response);
    }

    @Then("the report should contain product additions and removals")
    public void verifyProductMovements() {
        List<Object> additions = response.jsonPath().getList("additions");
        List<Object> removals = response.jsonPath().getList("removals");
        
        assertNotNull(additions);
        assertNotNull(removals);
    }

    @When("I request an inventory report in CSV format")
    public void requestCsvReport() {
        response = given()
                .header("Authorization", "Bearer " + token)
                .header("Accept", "text/csv")
                .get(BASE_URL + "/api/reports/export");
        
        assertNotNull(response);
    }

    @Then("the response content type should be {string}")
    public void verifyContentType(String contentType) {
        assertTrue(response.getContentType().contains(contentType));
    }

    @Then("the CSV should contain all product data")
    public void verifyCsvContent() {
        String csvContent = response.getBody().asString();
        assertNotNull(csvContent);
        assertTrue(csvContent.contains("id"));
        assertTrue(csvContent.contains("name"));
        assertTrue(csvContent.contains("stock"));
    }

    @When("I request an inventory report from {string} to {string}")
    public void requestDateRangeReport(String startDate, String endDate) {
        response = given()
                .header("Authorization", "Bearer " + token)
                .queryParam("startDate", startDate)
                .queryParam("endDate", endDate)
                .get(BASE_URL + "/api/reports/date-range");
        
        assertNotNull(response);
    }

    @Then("the report should only include data from the specified date range")
    public void verifyDateRangeData() {
        List<String> dates = response.jsonPath().getList("items.date");
        
        for (String date : dates) {
            // Verify date is within range - this is a simplified check
            assertTrue(date.compareTo(response.jsonPath().getString("startDate")) >= 0);
            assertTrue(date.compareTo(response.jsonPath().getString("endDate")) <= 0);
        }
    }
}
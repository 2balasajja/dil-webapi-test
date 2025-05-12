package utils;

import io.restassured.response.Response;
import org.dwp.model.Product;

import java.util.HashMap;
import java.util.Map;

/**
 * Context class to share state between step definitions.
 */
public class TestContext {
    private Response lastResponse;
    private Product currentProduct;
    private String currentProductId;
    private Map<String, Object> scenarioContext;

    public TestContext() {
        scenarioContext = new HashMap<>();
    }

    /**
     * Sets the last API response.
     *
     * @param response the response
     */
    public void setLastResponse(Response response) {
        this.lastResponse = response;
    }

    /**
     * Gets the last API response.
     *
     * @return the last response
     */
    public Response getLastResponse() {
        return lastResponse;
    }

    /**
     * Sets the current product.
     *
     * @param product the product
     */
    public void setCurrentProduct(Product product) {
        this.currentProduct = product;
    }

    /**
     * Gets the current product.
     *
     * @return the current product
     */
    public Product getCurrentProduct() {
        return currentProduct;
    }

    /**
     * Sets the current product ID.
     *
     * @param productId the product ID
     */
    public void setCurrentProductId(String productId) {
        this.currentProductId = productId;
    }

    /**
     * Gets the current product ID.
     *
     * @return the current product ID
     */
    public String getCurrentProductId() {
        return currentProductId;
    }

    /**
     * Sets a value in the scenario context.
     *
     * @param key   the key
     * @param value the value
     */
    public void setContext(String key, Object value) {
        scenarioContext.put(key, value);
    }

    /**
     * Gets a value from the scenario context.
     *
     * @param key          the key
     * @param defaultValue the default value
     * @return the value or default value if not found
     */
    public Object getContext(String key, Object defaultValue) {
        return scenarioContext.getOrDefault(key, defaultValue);
    }

    /**
     * Gets a value from the scenario context.
     *
     * @param key the key
     * @return the value
     */
    public Object getContext(String key) {
        return scenarioContext.get(key);
    }

    /**
     * Clears the scenario context.
     */
    public void clearContext() {
        scenarioContext.clear();
    }
}
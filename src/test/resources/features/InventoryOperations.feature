@InventoryOperations
Feature: Inventory Operations API Tests
  As an API user
  I want to perform inventory operations
  So that I can manage stock levels

  Background:
    Given the API base URL is set
    And I am authenticated with username "user01" and password "secpassword*"

  @Positive
  Scenario: Buy products and update inventory
    Given I have an existing product with sufficient stock
    When I send a request to buy 5 units of the product
    Then the response status code should be 200
    And the inventory should be updated with the new quantity
    And the response should contain the updated inventory details

  @Positive
  Scenario: Sell products and update inventory
    Given I have an existing product with sufficient stock
    When I send a request to sell 3 units of the product
    Then the response status code should be 200
    And the inventory should be updated with the new quantity
    And the response should contain the updated inventory details

  @Negative
  Scenario: Attempt to sell more products than available in stock
    Given I have an existing product with 10 units in stock
    When I send a request to sell 15 units of the product
    Then the response status code should be 400
    And the response should indicate insufficient stock
    And the inventory quantity should remain unchanged

  @Positive
  Scenario: Check stock levels for a product
    Given I have an existing product in the inventory
    When I send a request to check the stock level
    Then the response status code should be 200
    And the response should contain the current stock quantity

  @Negative
  Scenario: Attempt to check stock for a non-existent product
    Given I have a non-existent product ID
    When I send a request to check the stock level
    Then the response status code should be 404
    And the response should indicate the product was not found
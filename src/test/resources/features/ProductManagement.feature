@ProductManagement
Feature: Product Management API Tests
  As an API user
  I want to manage products in the inventory
  So that I can keep track of available items

  Background:
    Given the API base URL is set
    And I am authenticated with username "user01" and password "secpassword*"

  @Positive
  Scenario: Add a new product successfully
    Given I have a new product with the following details:
      | name        | Wireless Mouse      |
      | description | Bluetooth mouse     |
      | price       | 19.99               |
      | quantity    | 50                  |
      | category    | Computer Accessories|
    When I send a request to add the product
    Then the response status code should be 201
    And the response should contain the created product details
    And the product should be available in the inventory

  @Positive
  Scenario: Update an existing product successfully
    Given I have an existing product in the inventory
    When I update the product with the following details:
      | name        | Updated Product Name |
      | description | Updated description  |
      | price       | 29.99                |
      | quantity    | 100                  |
    Then the response status code should be 200
    And the response should contain the updated product details
    And the product details should be updated in the inventory

  @Positive
  Scenario: Delete a product successfully
    Given I have an existing product in the inventory
    When I send a request to delete the product
    Then the response status code should be 200
    And the product should be removed from the inventory

  @Negative
  Scenario: Attempt to add a product with missing required fields
    Given I have a new product with the following details:
      | name        |                     |
      | description | Bluetooth mouse     |
      | price       | 19.99               |
      | quantity    | 50                  |
    When I send a request to add the product
    Then the response status code should be 400
    And the response should contain validation error messages

  @Negative
  Scenario: Attempt to update a non-existent product
    Given I have a non-existent product ID
    When I update the product with the following details:
      | name        | Updated Product Name |
      | description | Updated description  |
      | price       | 29.99                |
      | quantity    | 100                  |
    Then the response status code should be 404
    And the response should indicate the product was not found
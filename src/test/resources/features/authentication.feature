@Authentication
Feature: Authentication API Tests
  As an API user
  I want to authenticate with the system
  So that I can access protected endpoints

  Background:
    Given the API base URL is set

  @Positive
  Scenario: Successful authentication with valid credentials
    When I authenticate with username "user01" and password "secpassword*"
    Then the authentication response status code should be 200
    And the authentication response should contain a valid token
    And the authentication response should contain user details

  @Negative
  Scenario: Failed authentication with invalid username
    When I authenticate with username "invaliduser" and password "secpassword*"
    Then the authentication response status code should be 401
    And the authentication response should contain an error message

  @Negative
  Scenario: Failed authentication with invalid password
    When I authenticate with username "user01" and password "wrongpassword"
    Then the authentication response status code should be 401
    And the authentication response should contain an error message

  @Negative
  Scenario: Failed authentication with empty credentials
    When I authenticate with username "" and password ""
    Then the authentication response status code should be 400
    And the authentication response should contain an error message
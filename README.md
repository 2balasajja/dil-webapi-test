# Inventory Management System API Testing Framework

This project provides an automated testing framework for the Inventory Management System API using Cucumber, JUnit 5, and REST Assured.

## Overview

This project is designed to test the Inventory Management System API endpoints using a behavior-driven development (BDD) approach. It uses Cucumber for writing test scenarios in a human-readable format and REST Assured for API testing.

## API Information

- **API URL**: https://apiforshopsinventorymanagementsystem.onrender.com/api-docs/#/
- **Description**: The Inventory Management System API facilitates adding, updating, deleting products and also enables buying or selling products.
- **Authentication**: Required for adding, deleting, or updating products
- **Features**: 
  - Product management (add, update, delete)
  - Inventory operations (buy, sell)
  - Stock level queries

## Authentication

Use any of the following credentials for authentication:

| Username | Password       |
|----------|----------------|
| user01   | secpassword*   |
| user02   | secpassword*   |
| user03   | secpassword*   |
| user04   | secpassword*   |
| user05   | secpassword*   |
| user06   | secpassword*   |
| user07   | secpassword*   |
| user08   | secpassword*   |
| user09   | secpassword*   |
| user010  | secpassword*   |

## Project Structure

```
.
├── src
│   └── test
│       ├── java
│       │   ├── org/dwp
│       │   │   └── model
│       │   ├── runners
│       │   ├── stepDefinitions
│       │   └── utils
│       └── resources
│           ├── config
│           ├── features
│           └── templates
└── pom.xml
```

## Key Components

- **Feature Files**: Located in `src/test/resources/features`, these files contain test scenarios written in Gherkin syntax.
- **Step Definitions**: Located in `src/test/java/stepDefinitions`, these Java classes implement the steps defined in feature files.
- **Test Runners**: Located in `src/test/java/runners`, these classes configure and run the Cucumber tests.
- **Utilities**: Located in `src/test/java/utils`, these classes provide supporting functionality for the tests.
- **Models**: Located in `src/test/java/org/dwp/model`, these classes represent the data structures used in the API.

## Features

The test suite includes the following feature files:

1. **Authentication.feature**: Tests for API authentication functionality
2. **ProductManagement.feature**: Tests for product CRUD operations
3. **InventoryOperations.feature**: Tests for buying and selling products and checking stock levels

## Prerequisites

- Java 17 or higher
- Maven 3.6 or higher

## Best Practices Implemented

1. **Reusability**:
   - Shared step definitions in base classes
   - Common utilities for API interactions
   - Reusable model classes for request/response objects

2. **Readability**:
   - BDD-style Gherkin syntax for test scenarios
   - Clear and descriptive step definitions
   - Meaningful method and variable names

3. **Maintainability**:
   - Separation of concerns (models, steps, utilities)
   - Configuration externalization
   - Consistent code style and documentation

4. **Test Coverage**:
   - Happy path scenarios (successful operations)
   - Unhappy path scenarios (error handling)
   - Edge cases and validation tests

## Configuration

The project uses configuration properties located in `src/test/resources/config/config.properties`. Environment-specific configurations can be set here.

Key configuration properties:

```properties
# API Configuration
api.base.url=https://apiforshopsinventorymanagementsystem.onrender.com
api.username=user01
api.password=secpassword*

# Timeout Configuration (in milliseconds)
api.timeout.connect=10000
api.timeout.read=30000

# Retry Configuration
api.retry.max=3
api.retry.delay=2000
```

## Running Tests

To run all tests:

```bash
mvn clean test
```

To run tests with specific tags:

```bash
mvn clean test -Dcucumber.filter.tags="@Authentication"
```

To run tests in a specific environment:

```bash
mvn clean test -Denvironment=dev
```

Available tags:
- `@Authentication`: Authentication tests
- `@ProductManagement`: Product management tests
- `@InventoryOperations`: Inventory operations tests
- `@Positive`: Happy path scenarios
- `@Negative`: Unhappy path scenarios

## Test Reports

After running tests, reports are generated in the following locations:

- HTML Report: `target/cucumber-reports.html`
- JSON Report: `target/cucumber-reports/cucumber-junit5.json`
- Detailed Reports: `target/cucumber-detailed-reports`
- Summary Report: `target/test-summary.html`

## Dependencies

- Cucumber 7.14.0
- JUnit 5.8.1
- REST Assured 4.3.3
- Jackson Databind 2.13.4.2
- Lombok 1.18.30

## Build and Plugins

The project uses Maven for build automation with the following key plugins:

- Maven Compiler Plugin (Java 17)
- Maven Surefire Plugin (for unit tests)
- Maven Failsafe Plugin (for integration tests)
- Cucumber Reporting Plugin (for enhanced test reports)

## Conclusion

This test framework provides comprehensive test coverage for the Inventory Management System API, including authentication, product management, and inventory operations. The BDD approach makes the tests readable and maintainable, while the modular structure allows for easy extension and modification.
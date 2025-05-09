# HMRC API Testing Framework

This project provides an automated testing framework for HMRC APIs using Cucumber, JUnit 5, and REST Assured.

## Overview

The `dil-hmrc-tests` project is designed to test HMRC API endpoints using a behavior-driven development (BDD) approach. It uses Cucumber for writing test scenarios in a human-readable format and REST Assured for API testing.

## Project Structure

```
.
├── src
│   └── test
│       ├── java
│       │   ├── org/dwp
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

## Prerequisites

- Java 17 or higher
- Maven 3.6 or higher

## Configuration

The project uses configuration properties located in `src/test/resources/config/config.properties`. Environment-specific configurations can be set here.

## Running Tests

To run all tests:

```bash
mvn clean test
```

To run tests with specific tags:

```bash
mvn clean test -Dcucumber.filter.tags="@test"
```

To run tests in a specific environment:

```bash
mvn clean test -Denvironment=dev
```

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


## Contributors

[Add contributor information here]
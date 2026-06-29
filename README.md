# Task for Flexilant Works

Maven-based Java test automation project for Flexilant. It includes API contract and response validation tests (REST Assured) and CSV header comparison tests (TestNG).

## Prerequisites

- Java 11+
- Maven 3.6+

## Tech stack


| Library       | Purpose                                                           |
| ------------- | ----------------------------------------------------------------- |
| TestNG        | Test framework                                                    |
| REST Assured  | API testing and JSON schema validation                            |
| OpenCSV       | CSV file handling                                                 |
| Allure TestNG | Test reporting                                                    |
| Logback       | Logging (rolling files under `logs/`, 10 MB max, 5-day retention) |
| Lombok        | Boilerplate reduction                                             |


## Project structure

```
task_for_flexilant_works/
├── src/
│   ├── main/java/com/flexilant/
│   │   ├── constants/          # Shared constants
│   │   ├── exceptions/         # Custom runtime exceptions
│   │   └── utils/              # File and test helpers
│   ├── main/resources/
│   │   ├── logback.xml
│   │   └── testng.xml
│   └── test/java/com/flexilant/
│       ├── CsvFileComparisonTest.java
│       ├── OrderDetailsVerificationTest.java
│       └── resources/json-schema/schema.json
├── test-data/                  # CSV files for comparison tests
└── pom.xml
```

## Test suites

### OrderDetailsVerificationTest

API tests for the orders endpoint using REST Assured. Configure the base URI in `setUp()` before running.


| Priority | Method                             | Description                                          |
| -------- | ---------------------------------- | ---------------------------------------------------- |
| 1        | `schemaValidation`                 | Validates response against `schema.json`             |
| 2        | `getOrderDetailsAndValidateStatus` | Asserts 200, required fields, and `status` is `paid` |
| 3        | `getOrderNotFound`                 | Asserts 404 and `Order not found` for unknown order  |
| 4        | `getOrderInvalidId`                | Asserts 400 for invalid `order_id` format            |
| 5        | `validateResponseTime`             | Asserts response time is under 2 seconds             |


**Endpoint:** `GET /api/orders/{order_id}`

**Schema location:** `src/test/java/resources/json-schema/schema.json`

### CsvFileComparisonTest

Validates CSV header comparison between expected and actual order files. Covers header extraction, common columns, relative ordering, and error handling for missing or empty files.

See [CsvFileComparisonTest.md](CsvFileComparisonTest.md) for full details, sample data, and expected behavior.


| Priority | Method                     | Description                                                 |
| -------- | -------------------------- | ----------------------------------------------------------- |
| 1        | `printBothHeader`          | Prints headers from both CSV files                          |
| 2        | `commonHeaders`            | Prints shared column names                                  |
| 3        | `headerSequence`           | Checks if any shared header appears at the same index       |
| 4        | `fileWithIdenticalHeaders` | Confirms identical headers when reading the same file twice |
| 5        | `missingFilePaths`         | Validates error when file path does not exist               |
| 6        | `emptyFileHandling`        | Validates error when file is empty                          |


**Test data:** `test-data/expected_orders.csv`, `test-data/actual_orders.csv`, `test-data/test_empty_file.csv`

## How to run

From the project root:

```bash
# Run all tests
mvn test

# Run a specific test class
mvn test -Dtest=OrderDetailsVerificationTest
mvn test -Dtest=CsvFileComparisonTest

# Run a single test method
mvn test -Dtest=CsvFileComparisonTest#printBothHeader
```

Tests can also be run from an IDE by executing the TestNG classes directly.

> **Note:** `OrderDetailsVerificationTest` requires a reachable API. Update the base URI in `OrderDetailsVerificationTest#setUp` before running those tests.

## Logging

Logs are written to `logs/application.log` with daily rolling and a 10 MB size limit. Archived logs are retained for 5 days. Configuration is in `src/main/resources/logback.xml`.

## Reporting

Allure results are generated under `allure-results/` when tests run with Allure enabled. Serve the report with the Allure CLI if installed:

```bash
allure serve allure-results
```


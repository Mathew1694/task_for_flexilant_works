# Task for Flexilant Works

Maven-based Java test automation project for Flexilant. It includes API contract and response validation tests (REST Assured) and CSV header comparison tests (TestNG).

## Prerequisites

- Java 17+
- Maven 3.6+

## Tech stack

| Library       | Purpose                                                                           |
| ------------- | --------------------------------------------------------------------------------- |
| TestNG        | Test framework and suite orchestration via [testng.xml](src/main/resources/testng.xml) |
| REST Assured  | API testing and JSON schema validation                                            |
| OpenCSV       | CSV file handling                                                                 |
| Allure TestNG | Test reporting via [AllureListener](src/main/java/com/flexilant/AllureListener.java) |
| Logback       | Logging to console and rolling files under `logs/` (10 MB max, 5-day retention)   |
| Lombok        | Boilerplate reduction (`@Slf4j` for logging)                                      |

## Project structure

```
task_for_flexilant_works/
├── src/
│   ├── main/java/com/flexilant/
│   │   ├── AllureListener.java   # Custom Allure TestNG listener
│   │   ├── constants/            # Shared constants
│   │   ├── exceptions/           # Custom runtime exceptions
│   │   └── utils/                # File and test helpers
│   ├── main/resources/
│   │   ├── logback.xml
│   │   └── testng.xml            # TestNG suite (used by Maven Surefire)
│   └── test/java/com/flexilant/
│       ├── AllureListener.java   # Custom Allure TestNG listener
│       ├── CsvFileComparisonTest.java
│       ├── OrderDetailsVerificationTest.java
│       └── resources/json-schema/schema.json
├── test-data/                    # CSV files for comparison tests
├── allure-results/               # Allure report data (created on test run)
├── logs/                         # Runtime log output (created on first run)
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

**Schema location:** [schema.json](src/test/java/resources/json-schema/schema.json)

**Configuration:** HTTP socket and connection timeouts are set to 60 seconds in `timeoutConfig()`. `ErrorLoggingFilter` logs failed REST Assured requests to the application logger.

### CsvFileComparisonTest

Validates CSV header comparison between expected and actual order files. Covers header extraction, common columns, relative ordering, and error handling for missing or empty files.

| Priority | Method                        | Description                                                                            |
|----------|-------------------------------|----------------------------------------------------------------------------------------|
| 1        | `printActualHeader`           | Logs headers from the actual CSV file                                                  |
| 2        | `printExpectedHeader`         | Logs headers from the expected CSV file                                                |
| 3        | `printBothHeader`             | Logs headers from both CSV files                                                       |
| 4        | `commonHeaders`               | Logs shared column names                                                               |
| 5        | `headerSequence`              | Checks if any shared header appears at the same index                                  |
| 6        | `fileWithIdenticalHeaders`    | Confirms identical headers when reading the same file twice                            |
| 7        | `missingFilePaths`            | Validates error when file path does not exist                                          |
| 8        | `emptyFileHandling`           | Validates error when file is empty                                                     |
| 9        | `sameHeaderDifferentSequence` | Validate If the headers are the same but in a different order, it should return equal. |
| 10       | `invalidHeaders`              | validate File with Invalid headers                                                     |

**Test data:** `test-data/expected_orders.csv`, `test-data/actual_orders.csv`, `test-data/test_empty_file.csv`

## How to run

From the project root:

```bash
# Run the TestNG suite (configured in testng.xml)
mvn test

# Use a different suite file
mvn test -DsuiteXmlFile=path/to/custom-suite.xml
```

Tests can also be run from an IDE by executing the TestNG suite or individual test classes directly.

> **Note:** `OrderDetailsVerificationTest` requires a reachable API. Update the base URI in `OrderDetailsVerificationTest#setUp` and enable the API test block in [testng.xml](src/main/resources/testng.xml) before running those tests.

## Logging

Logs are written to `logs/application.log` with daily rolling and a 10 MB size limit. Archived logs are retained for 5 days. Configuration is in [logback.xml](src/main/resources/logback.xml).

## Reporting

Allure results are written to `target/allure-results/` when tests run. Serve or generate the report with:

```bash
mvn allure:serve
# or
allure serve target/allure-results
```

Failed and skipped tests include **Test Metadata** and **Failure Stack Trace** attachments from [AllureListener](src/test/java/com/flexilant/AllureListener.java). API tests additionally include request/response details from `AllureRestAssured`.

**Personal Note** My SQL proficiency is currently at a basic to intermediate level. While working on SQL-related tasks, I occasionally rely on resources such as AI-assisted tools and Stack Overflow to validate approaches, explore alternative solutions, and enhance my understanding.
If the role requires SQL as a primary skill, I am confident in my ability to learn quickly, adapt to the required level of proficiency, and continuously improve through hands-on experience. I’m committed to continuously developing my technical skills.

**By submitting this exercise, I confirm that:**
1. I have followed the AI usage rules.
2. If I used AI, I have included the full relevant AI transcript or equivalent usage record.
3. I understand the submitted solution.
4. I am able to explain, modify, and debug the solution during the interview.
5. I understand that failure to disclose AI use, or inability to explain the submitted work, may result in rejection.
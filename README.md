
# GI Price Service

Spring Boot reactive application that provides a REST endpoint for querying prices of products for a specific date, product ID, and brand ID. The data is stored in an in-memory H2 database and initialized with the sample data provided below:

| BRAND_ID | START_DATE | END_DATE | FEE_ID | PRODUCT_ID | PRIORITY | PRICE | CURR |
|----------|------------|----------|--------|------------|----------|-------|------|
| 1 | 2020-06-14-00.00.00 | 2020-12-31-23.59.59 | 1 | 35455 | 0 | 35.50 | EUR |
| 1 | 2020-06-14-15.00.00 | 2020-06-14-18.30.00 | 2 | 35455 | 1 | 25.45 | EUR |
| 1 | 2020-06-15-00.00.00 | 2020-06-15-11.00.00 | 3 | 35455 | 1 | 30.50 | EUR |
| 1 | 2020-06-15-16.00.00 | 2020-12-31-23.59.59 | 4 | 35455 | 1 | 38.95 | EUR |

Where fields mean:

- BRAND_ID: company foreign key (1 = ZARA).
- START_DATE, END_DATE: range of dates in which the indicated price rate applies.
- FEE_ID: Applicable price list identifier.
- PRODUCT_ID: Product code identifier.
- PRIORITY: Price application disambiguator. If two rates coincide in a range of dates, the one with the highest priority (highest numerical value) is applied.
- PRICE: final sale price.
- CURR: iso of the currency.

## Getting Started

### Prerequisites

- Java 21 or higher (Spring Boot 4.x / WebFlux)
- Maven 3.6 or higher
- Docker (optional)
- Git (optional)
- Jenkins (optional)
- Sonar (optional)

### Application Design Decisions

##### Architecture

Hexagonal architecture: also known as ports and adapters, this architecture seeks maximum decoupling between the layers of the application. It consists of dividing the application into three layers: domain, application, and infrastructure. Each layer has its own responsibility and does not depend on the other layers. Dependencies are established through ports (interfaces) and adapters (implementations of those interfaces).

##### Paradigms, Conventions, Best practices and Technologies

- Pricing Rules Engine: the pricing selection logic is modelled as an ordered chain of `PricingRule` strategies (Strategy + Chain-of-Responsibility pattern). `PricingService` iterates the registered rules and delegates to the first one that declares itself applicable. Adding a new pricing strategy requires only implementing `PricingRule` and registering it in `GiPriceServiceApplicationConfig` — no changes to existing classes are needed (Open/Closed Principle). The current rule set is:
  - `HighestPriorityPricingRule` — catch-all fallback: selects the candidate with the greatest `PRIORITY` value.

- Clean Code: application development try to follow best practices for writing readable, maintainable, and scalable code. It includes principles such as the SOLID principle, the DRY (Don't Repeat Yourself) principle, the KISS (Keep It Simple and Straightforward) principle, and the YAGNI (You Ain't Gonna Need It) principle.

- Naming convention, applying next rules:
	- Use a clear and descriptive name that reflects the purpose of the application.
	- Use the standard package naming convention, e.g. com.itx.gipriceservice.
	- Use meaningful names for classes, methods, variables, and other elements in the code.
	- Follow the Java naming conventions for classes, e.g. use PascalCase for class names.

- Automated testing: unit tests, integration tests, and acceptance tests ensure code quality and detect errors early. In this case, application test development focuses on integration and acceptance tests pursuing correct implementation of the hexagonal architecture and achieve the highest possible percentage of coverage.

- Contract First and Documentation: using API First with OpenAPI and Spring Boot, development starts designing the API contract before any coding task. The contract is then used to generate code (src/main/java/com/itx/gipriceservice/infrastructure/in/api) and documentation automatically, saving time, reducing errors and also promotes consistency across APIs. Finally, it makes easier for developers to implement APIs.

- Reactive: Reactive programming is an approach to software development that emphasizes the use of asynchronous, non-blocking streams of data. It can be used to build highly scalable, responsive, and resilient applications that can handle large amounts of traffic and data. In the context of Spring Boot, reactive programming is supported through the Spring WebFlux module, which provides a reactive stack for building web applications using reactive principles. This allows for better resource utilization, lower latency and more efficient handling of I/O operations.

## Application

### Building the Application

To build the application, run the following command in the root directory:


		mvn clean install -DskipTests


This will compile the code and create an executable JAR file in the target directory.

### Running Tests

To run the tests, run the following command in the root directory:


		mvn test


This will execute application tests.

### Code Analysis <sub>1</sub>

To analyze the code, run the following command in the root directory (don't forget to replace **<sonarToken>** in the command):

        mvn sonar:sonar -Dsonar.host.url=http://<url>:<port> -Dsonar.login=<sonarToken> -Dsonar.coverage.exclusions=**/domain/model/**/*,**/infrastructure/in/api/**/*,**/infrastructure/in/controller/config/GlobalErrA*.*,**/infrastructure/out/persistence/entity/**/*

The analysis data should create/update the project information. Open `http://<url>:<port>/dashboard?id=com.itx:gi-price-service` in your browser to view it.

<sub>1</sub> [*Sonar must be locally installed and provide a valid sonar token credential replacing <sonarToken> with it in the command.*]

### Running the Application

To run the application, run the following command in the root directory:


		java -jar target/gi-price-service-1.0.0-SNAPSHOT.jar


Alternatively, you can use Docker<sub>2</sub> to run the application:


		docker build -t gi-price-service:1.0.0-SNAPSHOT .
		docker run -p 8080:8080 --name gi-price-service gi-price-service:1.0.0-SNAPSHOT

This will build a Docker image and start a container with the application running on port 8080.

<sub>2</sub> [*Docker must be locally installed.*]

### All in One <sub>3</sub>

To execute the Jenkinsfile implemented in the application:

1. Open Jenkins and create a new pipeline job.
2. In the pipeline configuration, select "Pipeline script from SCM" as the definition.
3. Choose Git as the SCM and enter the repository URL where the application source code is uploaded.
4. Specify the branch or tag containing the Jenkinsfile.
5. Specify the path where Jenkinsfile is located (src/main/resources/out/jenkins/Jenkinsfile).
6. Save the configuration and run the pipeline.

When the pipeline will be running, Jenkins will automatically retrieve the Jenkinsfile from the specified Git repository and execute it according to the pipeline configuration. If there are any errors or issues with the Jenkinsfile, Jenkins will provide feedback on how to correct them.

<sub>3</sub> [*Jenkins and Git must be locally installed and configured. At the same time the environment must be provisioned and well-configured too with all the tools specified in the prerequisites section.*]


### Accessing the H2 Console

The application uses a **pure in-memory** H2 database (`r2dbc:h2:mem:///gipriceservicedb`). This means two things that make traditional H2 console access impossible:

1. **In-memory databases are JVM-scoped**: the data lives exclusively inside the application's running process. A standalone H2 web console running in a separate process cannot connect to it — there is no file or TCP socket to attach to.
2. **Spring WebFlux does not support the H2 console servlet**: the H2 web console is a blocking servlet that is incompatible with the reactive (non-servlet) runtime.

For development-time inspection of executed queries and fetched rows, DEBUG-level logging is available for the persistence layer. Set the following in `application.properties`:

		logging.level.com.itx.gipriceservice.infrastructure.out.persistence=DEBUG

If direct database access is needed during development, the R2DBC URL can be temporarily switched to a file-based database:

		# application.properties (development only)
		spring.r2dbc.url=r2dbc:h2:file:///./gipriceservicedb

Then connect via a standalone H2 console with:

- Driver Class: `org.h2.Driver`
- JDBC URL: `jdbc:h2:file:./gipriceservicedb`
- User Name: `sa`
- Password: *(leave blank)*

> **Note**: The in-memory configuration is correct and intentional for this exercise — data is initialised on every startup and does not persist between runs.

## API

### API Documentation

API documentation is available in the Swagger UI. Open `http://localhost:8080/openapi-doc/openapi-ui.html` in your browser to view it.
Swagger JSON document is available too. Open `http://localhost:8080/openapi-doc/v3/api-docs` in your browser to view it.

### Accessing the API

Once the application is running, you can access the API using any HTTP client. The API has only one endpoint:


GET /v1.0/price?at={at}&productId={productId}&brandId={brandId}


The `at` parameter should be in ISO-8601 format with offset (`yyyy-MM-dd'T'HH:mm:ss.SSSZ`) and represents the date for which to query prices.

The `productId` parameter is a numeric identifier for the product.

The `brandId` parameter is a numeric identifier for the brand.

The response will be a JSON object with the following fields:

- `productId`: Numeric identifier of the product.
- `brandId`: Numeric identifier of the brand.
- `feeId`: Numeric identifier of the price list applied.
- `startDate`: Start date of the price list in ISO date-time format.
- `endDate`: End date of the price list in ISO date-time format.
- `cost`: Final sale price for the product.

### Postman Collection

There is no Postman collection in the source code. The following steps describe how to create one to test the application.

1. Save the commands below in a file and import it via **File → Import** in Postman. Postman will automatically create a collection with one request per `curl` command.

#### Group 1 — Price Resolution (Required Tests)

These five tests validate that the correct price is selected across different date/time scenarios. They cover both cases where a single rate is active and cases where two rates overlap (priority disambiguation).

		# Test 1: June 14 at 10:00 — only fee 1 covers this time → price 35.50
		curl -X 'GET' 'http://localhost:8080/v1.0/price?at=2020-06-14T10%3A00%3A00.000Z&productId=35455&brandId=1' -H 'accept: application/json'

		# Test 2: June 14 at 16:00 — fee 1 and fee 2 overlap; fee 2 wins (priority 1 > 0) → price 25.45
		curl -X 'GET' 'http://localhost:8080/v1.0/price?at=2020-06-14T16%3A00%3A00.000Z&productId=35455&brandId=1' -H 'accept: application/json'

		# Test 3: June 14 at 21:00 — fee 2 expired at 18:30; fee 1 resumes → price 35.50
		curl -X 'GET' 'http://localhost:8080/v1.0/price?at=2020-06-14T21%3A00%3A00.000Z&productId=35455&brandId=1' -H 'accept: application/json'

		# Test 4: June 15 at 10:00 — fee 3 active (00:00–11:00, priority 1) → price 30.50
		curl -X 'GET' 'http://localhost:8080/v1.0/price?at=2020-06-15T10%3A00%3A00.000Z&productId=35455&brandId=1' -H 'accept: application/json'

		# Test 5: June 16 at 21:00 — fee 4 active (from June 15 16:00, priority 1) → price 38.95
		curl -X 'GET' 'http://localhost:8080/v1.0/price?at=2020-06-16T21%3A00%3A00.000Z&productId=35455&brandId=1' -H 'accept: application/json'

#### Group 2 — Error Handling (Additional Tests)

These tests are not required by the specification but are included to validate the API contract beyond the happy path, verifying that the service behaves correctly when inputs are invalid or no matching data exists. A production-grade API must handle these scenarios explicitly.

		# Test 6: Missing required parameter 'at' → 400 Bad Request
		# Verifies that the endpoint rejects requests that do not supply all mandatory parameters.
		curl -X 'GET' 'http://localhost:8080/v1.0/price?productId=35455&brandId=1' -H 'accept: application/json'

		# Test 7: Date outside all price ranges → 404 Not Found
		# Verifies that the service returns a clear 404 instead of an empty body or 500
		# when no price row covers the requested date (year 2000, before any data exists).
		curl -X 'GET' 'http://localhost:8080/v1.0/price?at=2000-01-01T00%3A00%3A00.000Z&productId=35455&brandId=1' -H 'accept: application/json'

---

## Potential Improvements

The following improvements are listed in order of increasing complexity. Each addresses a real limitation of the current implementation.

---

### 1. Add a composite index on the query columns (Easy)

The database query filters on four columns simultaneously: `BRAND_ID`, `PRODUCT_ID`, `START_DATE`, and `END_DATE`. Without an index, H2 performs a full table scan on every request. Adding a composite index to `schema.sql` is a one-line change with a measurable impact at scale:

```sql
CREATE INDEX idx_prices_lookup ON PRICES (BRAND_ID, PRODUCT_ID, START_DATE, END_DATE);
```

The column order matters: place the equality-filter columns (`BRAND_ID`, `PRODUCT_ID`) first and the range-filter columns (`START_DATE`, `END_DATE`) last, so the index can eliminate rows efficiently before evaluating the date range.

---

### 2. Enforce hexagonal architecture boundaries with ArchUnit (Medium)

`archunit` is already declared as a test dependency in `pom.xml` but is not too much used. Writing a small set of ArchUnit rules would prevent architecture drift by making layer violations a failing test rather than a code-review finding. For example:

- Classes in `domain` must not import anything from `infrastructure` or `application`.
- Classes in `application` must not import anything from `infrastructure`.
- Domain ports (interfaces in `domain.port`) must only be implemented in `infrastructure.out`.

This costs a few dozen lines of test code and permanently guards the architectural decisions described in this README.

---


### 3. Add response caching (Hard)

The price endpoint is read-heavy: the same `(brandId, productId, at)` combination will be queried repeatedly and the underlying data changes infrequently. Adding a reactive caching layer would reduce database load and improve response latency significantly under real traffic.

The implementation would involve:

1. Choosing a cache store — **Caffeine** (in-process, zero infrastructure) for simplicity, or **Redis** (distributed, survives restarts) for production.
2. Introducing a `CachingPriceRepository` decorator that wraps `PriceRepositoryImpl` and intercepts calls to `findApplicablePrices`, returning a cached `Flux` when the key hits.
3. Defining a cache key from `(brandId, productId, at)` and a TTL aligned with the business cadence of price changes.
4. Adding a cache invalidation mechanism (e.g., an internal admin endpoint or a scheduled eviction) for when price data is updated.

The cache sits in the infrastructure layer as a second adapter for the `PriceRepository` port, requiring no changes to the domain or application layers.

---

### 4. Split into a multi-module Maven project (Hard)

The current single-module structure relies on package naming conventions and code reviews to enforce hexagonal architecture boundaries. Nothing in the build prevents a developer from adding an `import org.springframework.stereotype.Service` inside the `domain` package.

Restructuring into separate Maven modules makes the boundaries compile-time constraints:

```
gi-price-service/               ← parent POM
├── gi-price-service-domain/    ← no Spring, no R2DBC, no framework dependencies
├── gi-price-service-application/  ← depends on domain only
└── gi-price-service-infrastructure/  ← depends on domain + application; owns Spring wiring
```

The `domain` module's `pom.xml` would declare only `lombok` (compile-time) and standard Java libraries. Any attempt to import a Spring or R2DBC class would fail at compile time. This is the gold standard for enforcing hexagonal architecture in a Java project and makes the architecture guarantees structural rather than conventional.
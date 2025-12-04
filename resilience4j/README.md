# resilience4j

A small Spring Boot example app demonstrating Resilience4j features (CircuitBreaker, Retry, TimeLimiter, Bulkhead, RateLimiter), actuator metrics, and tests.

---

## Prerequisites

* Java 17+ (project is compatible with Java 17; Java 21 should work but POM targets 17)
* Maven 3.6+
* An IDE (IntelliJ IDEA recommended)

---

## Project layout (important files)

```
src/
  main/
    java/com/kadri/resilience4j/
      Resilience4jApplication.java
      config/RestTemplateConfig.java        # RestTemplate bean
      controller/DemoController.java        # example endpoints annotated with Resilience4j
    resources/
      application.yml                       # Resilience4j + actuator config
  test/
    java/.../Resilience4jApplicationTests.java   # integration tests using TestRestTemplate + WireMock
    resources/application-test.yml                # faster deterministic test settings
pom.xml
README.md
```

---

## How to run locally (IntelliJ)

1. Open IntelliJ and select **File → Open...** then choose the project root (where `pom.xml` is).
2. IntelliJ will import Maven automatically. If it doesn't, open the **Maven** tool window and click the refresh button.
3. Run the application: open `Resilience4jApplication.java` and click the green Run ▶ button next to the `main` method, or use **Run → Run 'Resilience4jApplication'**.
4. The app will start on `http://localhost:8080` (unless configured otherwise).

To run tests in IntelliJ: open the test class (e.g. `Resilience4jApplicationTests`) and click the green Run ▶ icon next to the class or method, or right-click and choose **Run**.

---

## Quick commands (Maven CLI)

```bash
# run tests
mvn test

# build artifact
mvn clean package

# run the app (from CLI)
mvn spring-boot:run
```

---

## Important endpoints to test

* Health: `GET http://localhost:8080/actuator/health`
* Metrics list: `GET http://localhost:8080/actuator/metrics`
* Circuit breaker metric: `GET http://localhost:8080/actuator/metrics/resilience4j.circuitbreaker.calls`
* Example endpoints (instrumented): `GET /api/circuit-breaker`, `/api/retry`, `/api/time-limiter`, `/api/bulkhead`

Use Postman, curl, or browser for `GET` endpoints. For curl example:

```bash
curl -i http://localhost:8080/api/circuit-breaker
curl http://localhost:8080/actuator/metrics/resilience4j.circuitbreaker.state | jq .
```

---

```

* `application-test.yml` contains shorter timeouts and smaller window sizes to make tests deterministic and fast.

---

## Swagger / OpenAPI

This project includes optional support for interactive API documentation using **Swagger UI** provided by **springdoc-openapi**.

### How to use

* After starting the application, visit:

    * **Swagger UI:** `http://localhost:8080/swagger-ui/index.html`
    * **OpenAPI JSON:** `http://localhost:8080/v3/api-docs`

### Dependency

```xml
<dependency>
  <groupId>org.springdoc</groupId>
  <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
  <version>2.3.0</version>
</dependency>
```
---

## Troubleshooting tips

* If `/v3/api-docs` or other endpoints return 500 — check application logs for stack traces. Many 500s come from library version mismatches (e.g., `NoSuchMethodError`) or model classes that cannot be introspected.
* If `TestRestTemplate` is not injected in tests: ensure you use `@SpringBootTest(webEnvironment = RANDOM_PORT)`.
* If Resilience4j behaviour looks wrong, check that the annotation `name` equals the instance key in `application.yml`.

---

## License & attribution

###### `© 2025 Yaakoub Kadri. All rights reserved.`


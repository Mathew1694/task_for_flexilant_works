package com.flexilant;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.config.HttpClientConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.filter.log.ErrorLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.nio.file.Path;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchema;
import static org.hamcrest.Matchers.*;

@Slf4j
public class OrderDetailsVerificationTest {
   private RequestSpecification requestSpecification;
   private ResponseSpecification responseSpecification;

   private RestAssuredConfig timeoutConfig() {
      int timeout = 60000;
      HttpClientConfig httpClientConfig = HttpClientConfig.httpClientConfig()
            .setParam("http.socket.timeout", timeout)
            .setParam("http.connection.timeout", timeout);
      RestAssured.config = RestAssuredConfig.config()
            .httpClient(httpClientConfig);
      return RestAssured.config();
   }

   @BeforeClass
   public void setUp() {
      requestSpecification = new RequestSpecBuilder()
            .setBaseUri("https://www...")
            .setConfig(timeoutConfig())
            .addFilter(new AllureRestAssured())
            .addFilter(new ErrorLoggingFilter())
            .setContentType(ContentType.JSON)
            .setAccept(ContentType.JSON)
            .build();

      responseSpecification = new ResponseSpecBuilder()
            .expectContentType(ContentType.JSON)
            .expectStatusCode(200)
            .expectResponseTime(lessThan(2000L))
            .build();
   }

   //contract validation is a process of verifying that the response of the API is matching with the expected schema.
   //it is a very important test to verify the response of the API is matching with the expected schema.
   //if the response is not matching with the expected schema, then the API is not working as expected.
   //if the response is matching with the expected schema, then the API is working as expected.
   @Test(priority = 1, description = "Validate if schema is matching in the response")
   public void schemaValidation() {
      String orderId = "ORD-1001";

      Response response = given()
            .spec(requestSpecification)
            .pathParams(Map.of("order_id", orderId))
            .get("/api/orders");
      Path path = Path.of("src", "test", "resources", "json-schema", "schema.json");
      response.then().body(matchesJsonSchema(path.toFile()));
   }


   @Test(priority = 2, description = "Validate all values are present in the response, And http status is 200 and status is paid")
   public void getOrderDetailsAndValidateStatus() {
      String orderId = "ORD-1001";

      Response response = given()
            .spec(requestSpecification)
            .pathParam("order_id", orderId)
            .when()
            .get("/api/orders");

      response.then()
            .statusCode(200)
            .body("order_id", equalTo(orderId))
            .body("customer_id", notNullValue())
            .body("amount", notNullValue())
            .body("currency", equalTo("GBP"))
            .body("status", equalTo("paid"))
            .body("created_at", notNullValue());
   }

   @Test(priority = 3, description = "Validate if order is not found then 404 status code is returned")
   public void getOrderNotFound() {
      String orderId = "ORD-9999";

      Response response = given()
            .spec(requestSpecification)
            .pathParam("order_id", orderId)
            .when()
            .get("/api/orders");

      response.then()
            .statusCode(404)
            .body("error", equalTo("Order not found"));
   }

   @Test(priority = 4, description = "Validate bad request if order_id data type is mismatching, should return 400 status code")
   public void getOrderInvalidId() {
      int orderId = 458334;

      Response response = given()
            .spec(requestSpecification)
            .pathParam("order_id", orderId)
            .when()
            .get("/api/orders");

      response.then()
            .statusCode(400)
            .body("error", equalTo("Invalid order_id format"));
   }

   // response time should be less than 2 seconds
   @Test(priority = 5, description = "Validate API response time is within acceptable limit")
   public void validateResponseTime() {
      String orderId = "ORD-1001";

      Response response = given()
            .spec(requestSpecification)
            .pathParam("order_id", orderId)
            .when()
            .get("/api/orders");

      response.then().spec(responseSpecification);
   }

}

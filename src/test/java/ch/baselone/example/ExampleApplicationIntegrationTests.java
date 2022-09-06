package ch.baselone.example;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;

import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;

import static org.hamcrest.Matchers.equalTo;;



@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class ExampleApplicationIntegrationTests {

	@LocalServerPort
	private Integer port;
		
	@Test
	void test() {
		RequestSpecification request = RestAssured.given().port(port);
		ValidatableResponse response = request.get("/hello").then();

		response.statusCode(200);
		response.body(equalTo("Hello World"));
	}
}
package ch.baselone.example;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import static io.restassured.RestAssured.get;
import static org.hamcrest.Matchers.equalTo;;



@SpringBootTest
class ExampleApplicationIntegrationTests {

	@Test
	void test() {
		get("/hello")
		.then()
			.statusCode(200)
			.assertThat().body(equalTo("hello"));
	}
}
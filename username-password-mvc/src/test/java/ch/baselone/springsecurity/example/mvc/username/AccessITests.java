package ch.baselone.springsecurity.example.mvc.username;

import io.restassured.module.mockmvc.RestAssuredMockMvc;
import io.restassured.module.mockmvc.response.MockMvcResponse;
import io.restassured.module.mockmvc.response.ValidatableMockMvcResponse;
import org.apache.http.HttpHeaders;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.StringEndsWith.endsWith;


@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class AccessITests {
    AccessITests(WebApplicationContext webApplicationContext) {
        RestAssuredMockMvc.webAppContextSetup(webApplicationContext);
    }

    @Test
    void contextLoads() {
    }

    @Test
    void debugReachableWithoutAuthentication() {
        MockMvcResponse response = RestAssuredMockMvc.get("/debug/filters");

        ValidatableMockMvcResponse validateResponse = response.then();
        validateResponse.statusCode(200);
    }

    @Test
    void notAuthenticated() {
        MockMvcResponse response = RestAssuredMockMvc.get("/hello/user");

        ValidatableMockMvcResponse validateResponse = response.then();
        validateResponse.statusCode(302);
        validateResponse.header(HttpHeaders.LOCATION, endsWith("/login"));
    }

    @Test
    @WithMockUser
    void authenticated() {
        MockMvcResponse response = RestAssuredMockMvc.get("/hello/user");

        ValidatableMockMvcResponse validateResponse = response.then();
        validateResponse.statusCode(200);
        validateResponse.body(equalTo("Hello user"));
    }

    @Test
    @WithMockUser
    void notAuthorized() {
        MockMvcResponse response = RestAssuredMockMvc.get("/hello/admin");

        ValidatableMockMvcResponse validateResponse = response.then();
        validateResponse.statusCode(403);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void authorized() {
        MockMvcResponse response = RestAssuredMockMvc.get("/hello/admin");

        ValidatableMockMvcResponse validateResponse = response.then();
        validateResponse.statusCode(200);
        validateResponse.body(equalTo("Admin Hello user"));
    }


    @Test
    @WithMockUser(roles = "ADMIN")
    void defaultBlocked() {
        MockMvcResponse response = RestAssuredMockMvc.get("/else");

        ValidatableMockMvcResponse validateResponse = response.then();
        validateResponse.statusCode(403);
    }


}
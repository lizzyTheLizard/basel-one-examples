package ch.baselone.springsecurity.example.flux.simple;


import org.hamcrest.core.StringStartsWith;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class AccessITests {
    private final WebTestClient webTestClient;

    AccessITests(ApplicationContext applicationContext) {
        webTestClient = WebTestClient.bindToApplicationContext(applicationContext).build();
    }

    @Test
    void contextLoads() {
    }

    @Test
    void notAuthenticated() {
        webTestClient
                .get().uri("/hello")
                .exchange()
                .expectStatus().is4xxClientError();
    }

    @Test
    @WithMockUser(username = "testuser")
    void authenticated() {
        webTestClient
                .get().uri("/hello")
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody(String.class).value(StringStartsWith.startsWith("Hello "));
    }
}
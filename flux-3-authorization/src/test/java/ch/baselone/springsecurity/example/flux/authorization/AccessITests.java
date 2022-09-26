package ch.baselone.springsecurity.example.flux.authorization;

import org.hamcrest.core.StringStartsWith;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.context.ApplicationContext;
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
                .get().uri("/hello/user")
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().location("/login");
    }

    @Test
    @WithMockUser(username = "testuser")
    void authenticated() {
        webTestClient
                .get().uri("/hello/user")
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody(String.class)
                .value(StringStartsWith.startsWith("Hello "));
    }

    @Test
    @WithMockUser(username = "testuser")
    void notAuthorized() {
        webTestClient
                .get().uri("/hello/admin")
                .exchange()
                .expectStatus().is4xxClientError();
    }

    @Test
    @WithMockUser(username = "testuser", authorities = "ROLE_ADMIN")
    void authorized() {
        webTestClient
                .get().uri("/hello/admin")
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody(String.class)
                .value(StringStartsWith.startsWith("Admin Hello "));
    }
}
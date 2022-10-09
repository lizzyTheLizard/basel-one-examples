package ch.baselone.springsecurity.example;


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
    @SuppressWarnings("EmptyMethod")
    void contextLoads() {
    }

    @Test
    @WithMockUser(username = "testuser")
    void notAllowed() {
        webTestClient
                .get().uri("/hello?ret=2")
                .exchange()
                .expectStatus().isForbidden();
    }

    @Test
    @WithMockUser(username = "testuser")
    void allowed() {
        webTestClient
                .get().uri("/hello?ret=5")
                .exchange()
                .expectStatus().is2xxSuccessful();
    }
}
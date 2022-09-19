package ch.baselone.springsecurity.example.flux.username;


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
    void debugReachableWithoutAuthentication() {
        webTestClient
                .get().uri("/debug/filters")
                .exchange()
                .expectStatus().is2xxSuccessful();
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
                .expectBody(String.class).isEqualTo("Hello testuser");
    }

    @Test
    @WithMockUser
    void notAuthorized() {
        webTestClient
                .get().uri("/hello/admin")
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    @WithMockUser(roles = "ADMIN", username = "testuser")
    void authorized() {
        webTestClient
                .get().uri("/hello/admin")
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody(String.class).isEqualTo("Admin Hello testuser");
    }

    @Test
    @WithMockUser(roles = "ADMIN", username = "testuser")
    void defaultBlocked() {
        webTestClient
                .get().uri("/else")
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.FORBIDDEN);
    }
}
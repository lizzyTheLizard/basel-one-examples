package ch.baselone.springsecurity.example.flux.oidc.client;

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
    void redirectToAuthPage() {
        webTestClient
                .get().uri("/hello")
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().valueEquals("Location", "/oauth2/authorization/keycloak");
    }

    @Test
    void redirectToLogin() {
        webTestClient
                .get().uri("/oauth2/authorization/keycloak")
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().value("Location", StringStartsWith.startsWith("http://localhost:8090/auth/realms/Test-Application/protocol/openid-connect/auth"));
    }

    @Test
    @WithMockUser
    void withAuthentication() {
        webTestClient
                .get().uri("/hello")
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody(String.class).value(StringStartsWith.startsWith("Hello "));
    }
}
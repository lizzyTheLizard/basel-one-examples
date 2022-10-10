package ch.baselone.springsecurity.example;

import org.hamcrest.core.IsEqual;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.List;

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
    @WithMockUser(username = "user")
    void readUser() {
        webTestClient
                .get().uri("/entities")
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBodyList(Entity.class).isEqualTo(List.of(Entity.getByIdBlocking(0)));
    }

    @Test
    @WithMockUser(username = "admin")
    void readAdmin() {
        webTestClient
                .get().uri("/entities")
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBodyList(Entity.class).isEqualTo(List.of(Entity.getByIdBlocking(0), Entity.getByIdBlocking(1)));
    }

    @Test
    @WithMockUser(username = "user")
    void readSingleFailed() {
        webTestClient
                .get().uri("/entities/1")
                .exchange()
                .expectStatus().isForbidden();
    }

    @Test
    @WithMockUser(username = "admin")
    void readSingleAllowed() {
        webTestClient
                .get().uri("/entities/1")
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody(Entity.class).isEqualTo(Entity.getByIdBlocking(1));
    }

    @Test
    @WithMockUser(username = "user")
    void updateFailed() {
        webTestClient
                .mutateWith(SecurityMockServerConfigurers.csrf())
                .post().uri("/entities/0")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just("{\"name\": \"zero2\",\"permission\":{\"user\":[\"READ\"],\"admin\": [\"READ\",\"WRITE\"]}}"), String.class)
                .exchange()
                .expectStatus().isForbidden();
    }

    @Test
    @WithMockUser(username = "admin")
    void updateAllowed() {
        webTestClient
                .mutateWith(SecurityMockServerConfigurers.csrf())
                .post().uri("/entities/0")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just("{\"name\": \"zero2\",\"permission\":{\"user\":[\"READ\"],\"admin\": [\"READ\",\"WRITE\"]}}"), String.class)
                .exchange()
                .expectStatus().is2xxSuccessful();
        webTestClient
                .get().uri("/entities/0")
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody(Entity.class).value(Entity::name, IsEqual.equalTo("zero2"));
    }
}
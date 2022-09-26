package ch.baselone.springsecurity.example.flux.protection;

import org.hamcrest.core.IsEqual;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

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
    void corsNotAllowed() {
        webTestClient
                .options().uri("https://url.com/csrf/none")
                .header("Origin", "https://test.com")
                .header("Access-Control-Request-Method", "GET")
                .exchange()
                .expectStatus().is4xxClientError()
                .expectHeader().doesNotExist("Access-Control-Allow-Origin");
    }

    @Test
    void corsAllowed() {
        webTestClient
                .options().uri("https://url.com/csrf/none")
                .header("Origin", "https://example.com")
                .header("Access-Control-Request-Method", "GET")
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectHeader().valueEquals("Access-Control-Allow-Origin","https://example.com");
    }

    @Test
    void contentSecurity() {
        webTestClient
                .options().uri("/csrf/none")
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectHeader().valueEquals("X-Frame-Options","DENY");
    }

    @Test
    void csrf() {
        EntityExchangeResult<String> formResult = webTestClient
                .get().uri("/csrf/token")
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody(String.class)
                .returnResult();
        String token = getToken(Objects.requireNonNull(formResult.getResponseBody()));
        Map<String, String> cookies = formResult.getResponseCookies()
                .entrySet().stream()
                .flatMap(e -> e.getValue().stream().map(c -> Map.entry(e.getKey(), c.getValue())))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        webTestClient
                .post().uri("/csrf")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .cookies(map -> cookies.forEach(map::add))
                .body(BodyInserters.fromFormData("name", "Name")
                        .with("_csrf", token))
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody(String.class).value(IsEqual.equalTo("Hello Name"));
    }

    private String getToken(String form) {
        int startPosition = form.indexOf("name=\"_csrf\" value=\"") + 20;
        String formEnd = form.substring(startPosition);
        int end = formEnd.indexOf("\"");
        return formEnd.substring(0, end);
    }
}
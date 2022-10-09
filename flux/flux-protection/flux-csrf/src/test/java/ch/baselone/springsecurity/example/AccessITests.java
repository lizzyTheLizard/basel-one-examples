package ch.baselone.springsecurity.example;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.test.web.reactive.server.FluxExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.util.MultiValueMap;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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
    void csrfMissing() {
        webTestClient
                .post().uri("/csrf")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(Mono.just("name=Name"), String.class)
                .exchange()
                .expectStatus().is4xxClientError();
    }

    @Test
    void csrf() {
        FluxExchangeResult<String> result = webTestClient
                .get().uri("/csrf/token")
                .exchange()
                .expectStatus().is2xxSuccessful()
                .returnResult(String.class);
        MultiValueMap<String, ResponseCookie> cookies = result.getResponseCookies();
        Mono<String> token = getToken(result.getResponseBody());

        webTestClient
                .post().uri("/csrf")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(token.map(t -> "name=Name&_csrf=" + t), String.class)
                .cookies(m -> cookies.forEach((n, vs) -> vs.forEach(v -> m.add(n, v.getValue()))))
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody(String.class).isEqualTo("Hello Name");
    }

    private Mono<String> getToken(Flux<String> fluxForm) {
        return fluxForm.collect(StringBuilder::new, StringBuilder::append).map(sb -> {
            String form = sb.toString();
            int startPosition = form.indexOf("name=\"_csrf\" value=\"") + 20;
            String formEnd = form.substring(startPosition);
            int end = formEnd.indexOf("\"");
            return formEnd.substring(0, end);
        });
    }
}
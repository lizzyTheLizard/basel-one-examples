package ch.baselone.springsecurity.example.flux.protection;

import org.springframework.security.web.server.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping
public class Controller {

    @GetMapping("csrf/none")
    public String noToken() {
        return """
                    <html>
                    <body>
                    <form action="/csrf" method="POST">
                      <label for="name">Name:</label><br>
                      <input type="text" id="name" name="name" value="John"><br>
                      <input type="submit" value="Submit">
                    </form>
                    </body>
                    </html>
                """;
    }

    @GetMapping("csrf/token")
    public Mono<String> token(ServerWebExchange exchange) {
        return getCsrfToken(exchange).map(t -> """
                    <html>
                    <body>
                    <form action="/csrf" method="POST">
                      <label for="name">Name:</label><br>
                      <input type="text" id="name" name="name" value="John"><br>
                      <input type="hidden" name="%s" value="%s"/>
                      <input type="submit" value="Submit">
                    </form>
                    </body>
                    </html>
                """.formatted(t.getParameterName(), t.getToken()));
    }

    private Mono<CsrfToken> getCsrfToken(ServerWebExchange exchange) {
        Mono<CsrfToken> token = exchange.getAttributeOrDefault(CsrfToken.class.getName(), Mono.empty());
        return token.switchIfEmpty(Mono.error(new RuntimeException("No CSRF-Token found")));
    }

    @RequestMapping("csrf")
    public Mono<String> form(ServerWebExchange exchange) {
        return exchange.getFormData()
                .map(formData -> "Hello " + formData.getFirst("name"));
    }
}

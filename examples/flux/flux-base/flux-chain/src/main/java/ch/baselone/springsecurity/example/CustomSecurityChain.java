package ch.baselone.springsecurity.example;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Create your own simple security chain. It will do nothing
 */
@Configuration
public class CustomSecurityChain implements SecurityWebFilterChain {
    @Override
    public Mono<Boolean> matches(ServerWebExchange exchange) {
        //Match all requests
        return Mono.just(true);
    }

    @Override
    public Flux<WebFilter> getWebFilters() {
        //Do nothing
        return Flux.empty();
    }
}

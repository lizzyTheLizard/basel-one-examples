package ch.baselone.springsecurity.example.flux.oidc.client;

import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping
public class Controller {
    @RequestMapping("hello")
    public Mono<String> hello() {
        return ReactiveSecurityContextHolder.getContext().map(c -> {
            String username = c.getAuthentication().getPrincipal().toString();
            return "Hello " + username;
        });
    }
}

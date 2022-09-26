package ch.baselone.springsecurity.example.flux.nosec;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("hello")
public class Controller {

    @RequestMapping
    public Mono<String> hello() {
        return Mono.just("Hello Unauthenticated User");
    }
}

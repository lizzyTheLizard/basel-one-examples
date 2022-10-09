package ch.baselone.springsecurity.example;

import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping
public class Controller {
    @RequestMapping("hello")
    @PostAuthorize("@permissionChecker.check(returnObject)")
    //Must be a reactive type for annotations to work
    public Mono<Integer> own(@RequestParam int ret) {
        return Mono.just(ret);
    }
}

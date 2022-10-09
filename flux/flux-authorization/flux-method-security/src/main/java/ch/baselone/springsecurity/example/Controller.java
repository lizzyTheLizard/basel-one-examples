package ch.baselone.springsecurity.example;

import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.Random;

@RestController
@RequestMapping
public class Controller {
    private final Random random = new Random();

    @RequestMapping("hello/user")
    @PreAuthorize("hasRole('USER')")
    public Mono<String> userHello() {
        return ReactiveSecurityContextHolder.getContext()
                .map(c -> "Hello " + c.getAuthentication().getName());
    }

    @RequestMapping("hello/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public Mono<String> adminHello() {
        return ReactiveSecurityContextHolder.getContext()
                .map(c -> "Admin Hello " + c.getAuthentication().getName());
    }

    @RequestMapping("post")
    @PostAuthorize("returnObject > 3")
    public int post() {
        return new Random().nextInt(10);
    }

    @RequestMapping("postfilter")
    @PostFilter("filterObject > 3")
    public int[] postFilter() {
        return new int[]{
                random.nextInt(10),
                random.nextInt(10),
                random.nextInt(10),
                random.nextInt(10)
        };
    }
}

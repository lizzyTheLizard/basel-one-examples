package ch.baselone.springsecurity.example.flux.authorization;

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
        return ReactiveSecurityContextHolder.getContext().map(c -> {
            String username = c.getAuthentication().getPrincipal().toString();
            return "Hello " + username;
        });
    }

    @RequestMapping("hello/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public Mono<String> adminHello() {
        return ReactiveSecurityContextHolder.getContext().map(c -> {
            String username = c.getAuthentication().getPrincipal().toString();
            return "Admin Hello " + username;
        });
    }

    @RequestMapping("post")
    @PostAuthorize("returnObject > 3")
    public Mono<Integer> post() {
        return Mono.just(new Random().nextInt(10));
    }

    @RequestMapping("postfilter")
    @PostFilter("filterObject > 3")
    public Mono<Integer[]> postFilter() {
        return Mono.just(new Integer[]{
                        random.nextInt(10),
                        random.nextInt(10),
                        random.nextInt(10),
                        random.nextInt(10)
                }
        );
    }

    @RequestMapping("own")
    @PostAuthorize("@permissionChecker.check(returnObject)")
    public Mono<Integer> own() {
        return Mono.just(new Random().nextInt(10));
    }
}

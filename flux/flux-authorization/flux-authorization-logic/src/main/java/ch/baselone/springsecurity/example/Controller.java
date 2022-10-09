package ch.baselone.springsecurity.example;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping
public class Controller {
    @RequestMapping("hello")
    public Mono<String> hello() {
        return ReactiveSecurityContextHolder.getContext()
                .map(this::ensureIsAdmin)
                .map(c -> "Hello " + c.getAuthentication().getName())
                .switchIfEmpty(Mono.error(new AccessDeniedException("Only Admin Allowed")));
    }

    private SecurityContext ensureIsAdmin(SecurityContext a) {
        if (a.getAuthentication().getAuthorities().stream().noneMatch(g -> g.getAuthority().equals("ROLE_ADMIN"))) {
            throw new AccessDeniedException("Only Admin Allowed");
        }
        return a;
    }
}

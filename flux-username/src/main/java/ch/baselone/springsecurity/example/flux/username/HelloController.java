package ch.baselone.springsecurity.example.flux.username;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.security.Principal;

@RestController
@RequestMapping("/hello")
public class HelloController {
    @GetMapping("user")
    public String hello(Principal principal) {
        return "Hello " + principal.getName();
    }

    @GetMapping("admin")
    @PreAuthorize("hasRole('ADMIN')")
    public Mono<String> helloAdmin(Principal principal) {
        return Mono.just("Admin Hello " + principal.getName());
    }
}

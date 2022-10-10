package ch.baselone.springsecurity.example;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Optional;

@RestController
@RequestMapping
public class Controller {
    @RequestMapping("hello")
    public String hello() {
        Optional<Authentication> authentication = Optional.ofNullable(SecurityContextHolder.getContext()
                .getAuthentication());
        return authentication
                .map(Principal::getName)
                .map(username -> "Hello " + username)
                .orElse("Hello unauthenticated");
    }
}

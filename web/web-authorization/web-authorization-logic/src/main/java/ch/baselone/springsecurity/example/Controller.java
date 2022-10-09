package ch.baselone.springsecurity.example;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping
public class Controller {
    @RequestMapping("hello")
    public String hello() {
        Authentication authentication = Optional.ofNullable(SecurityContextHolder.getContext()
                .getAuthentication()).orElseThrow(() -> new AccessDeniedException("Only Admin Allowed"));
        if (authentication.getAuthorities().stream().noneMatch(g -> g.getAuthority().equals("ROLE_ADMIN"))) {
            throw new AccessDeniedException("Only Admin Allowed");
        }
        String username = authentication.getName();
        return "Hello " + username;
    }
}

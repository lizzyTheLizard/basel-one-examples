package ch.baselone.springsecurity.example.mvc.username;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public String helloAdmin(Principal principal) {
        return "Admin Hello " + principal.getName();
    }
}

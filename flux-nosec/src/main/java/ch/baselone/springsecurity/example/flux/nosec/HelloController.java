package ch.baselone.springsecurity.example.flux.nosec;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("hello")
public class HelloController {

    @RequestMapping
    public String hello(Principal principal) {
        String username = principal == null ? "Unauthenticated User" : principal.getName();
        return "Hello " + username;
    }
}

package ch.baselone.springsecurity.example;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class Controller {
    @RequestMapping("hello")
    public String hello() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return "Hello " + username;
    }
}

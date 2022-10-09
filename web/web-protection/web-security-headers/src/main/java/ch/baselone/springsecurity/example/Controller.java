package ch.baselone.springsecurity.example;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("hello")
public class Controller {
    @RequestMapping
    @SuppressWarnings("SameReturnValue")
    public String hello() {
        return "Hello Unauthenticated User";
    }
}

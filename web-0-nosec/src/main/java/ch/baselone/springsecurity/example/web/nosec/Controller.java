package ch.baselone.springsecurity.example.web.nosec;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("hello")
public class Controller {

    @RequestMapping
    public String hello() {
        return "Hello Unauthenticated User";
    }
}

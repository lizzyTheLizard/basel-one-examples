package ch.baselone.example;

import org.springframework.web.bind.annotation.RestController;

@RestController("hello")
public class ExampleController {
    public String hello(){
        return "Hello World";
    }
}

package ch.baselone.springsecurity.mistake;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {
    @RequestMapping("/internal/test")
    public String internalEndpoint(){
        return "Internal";
    }

    @RequestMapping("/test")
    public String publicEndpoint(){
        return "Public";
    }

}

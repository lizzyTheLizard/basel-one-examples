package ch.baselone.springsecurity.example;

import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class Controller {
    @RequestMapping("hello")
    @PostAuthorize("@permissionChecker.check(returnObject)")
    public int own(@RequestParam int ret) {
        return ret;
    }
}

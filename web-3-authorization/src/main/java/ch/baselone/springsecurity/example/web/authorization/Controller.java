package ch.baselone.springsecurity.example.web.authorization;

import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

@RestController
@RequestMapping
public class Controller {
    private final Random random = new Random();

    @RequestMapping("hello/user")
    @PreAuthorize("hasRole('USER')")
    public String userHello() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return "Hello " + username;
    }

    @RequestMapping("hello/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminHello() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return "Admin Hello " + username;
    }

    @RequestMapping("post")
    @PostAuthorize("returnObject > 3")
    public int post() {
        return new Random().nextInt(10);
    }

    @RequestMapping("postfilter")
    @PostFilter("filterObject > 3")
    public int[] postFilter() {
        return new int[]{
                random.nextInt(10),
                random.nextInt(10),
                random.nextInt(10),
                random.nextInt(10)
        };
    }

    @RequestMapping("own")
    @PostAuthorize("@permissionChecker.check(returnObject)")
    public int own() {
        return new Random().nextInt(10);
    }
}

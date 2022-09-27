package ch.baselone.springsecurity.example.web.authorization;

import org.springframework.stereotype.Component;

@Component
public class PermissionChecker {
    public boolean check(Object obj) {
        return ((Integer) obj > 3);
    }
}

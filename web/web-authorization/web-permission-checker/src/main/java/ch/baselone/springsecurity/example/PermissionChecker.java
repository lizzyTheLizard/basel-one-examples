package ch.baselone.springsecurity.example;

import org.springframework.stereotype.Component;

@Component
public class PermissionChecker {
    public boolean check(Object obj) {
        return ((Integer) obj > 3);
    }
}

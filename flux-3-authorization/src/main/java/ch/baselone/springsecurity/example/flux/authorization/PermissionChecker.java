package ch.baselone.springsecurity.example.flux.authorization;

import org.springframework.stereotype.Component;

@Component
public class PermissionChecker {
    //Having reactive permission checker (e.g. return a Mono here) is only supported with Spring 5.8
    public boolean check(Object obj) {
        return ((Integer) obj > 3);
    }
}

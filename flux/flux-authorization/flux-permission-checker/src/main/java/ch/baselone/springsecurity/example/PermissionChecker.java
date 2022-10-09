package ch.baselone.springsecurity.example;

import org.springframework.stereotype.Component;

@Component
//Reactive checker are not supported with Spring 5.7, but this will likely be added in Spring 6
public class PermissionChecker {
    public boolean check(Object obj) {
        return ((Integer) obj > 3);
    }
}

package ch.baselone.springsecurity.example;

import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Optional;

//Unfortunately reactive permission checks are not possible in Spring 5.7. This might be added in Spring 6
@Component
public class CustomPermissionEvaluator implements PermissionEvaluator {
    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
        Entity entity = (Entity) targetDomainObject;
        Permission entityPermission = (Permission) permission;
        return Optional.of(entity)
                .map(e -> e.permission().get(authentication.getName()))
                .map(p -> p.contains(entityPermission))
                .orElse(false);
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
        int entityId = (int) targetId;
        Permission entityPermission = (Permission) permission;
        return Optional.ofNullable(Entity.getByIdBlocking(entityId))
                .map(e -> e.permission().get(authentication.getName()))
                .map(p -> p.contains(entityPermission))
                .orElse(false);
    }
}

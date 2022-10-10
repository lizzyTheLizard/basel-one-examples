package ch.baselone.springsecurity.example;


import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public record Entity(String name, Map<String, List<Permission>> permission) {
    private static final Map<Integer, Entity> repository = new HashMap<>();

    static {
        repository.put(0, new Entity("zero", Map.of("user", List.of(Permission.READ), "admin", List.of(Permission.READ, Permission.WRITE))));
        repository.put(1, new Entity("one", Map.of("admin", List.of(Permission.READ, Permission.WRITE))));
    }

    public static Mono<Entity> getById(int id) {
        return Mono.just(repository.get(id));
    }

    public static Entity getByIdBlocking(int id) {
        return repository.get(id);
    }

    public static Flux<Entity> getAll() {
        return Flux.just(repository.values().toArray(Entity[]::new));
    }

    public static Mono<Void> update(int id, Entity newEntity) {
        repository.put(id, newEntity);
        return Mono.just(true).then();
    }
}

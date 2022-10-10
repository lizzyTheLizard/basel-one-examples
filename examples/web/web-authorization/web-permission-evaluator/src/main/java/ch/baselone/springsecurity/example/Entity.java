package ch.baselone.springsecurity.example;


import java.util.*;

public record Entity(String name, Map<String, List<Permission>> permission) {
    private static final Map<Integer, Entity> repository = new HashMap<>();

    static{
        repository.put(0, new Entity("zero", Map.of("user", List.of(Permission.READ), "admin", List.of(Permission.READ, Permission.WRITE))));
        repository.put(1, new Entity("one", Map.of("admin", List.of(Permission.READ, Permission.WRITE))));
    }

    public static Optional<Entity> getById(int id) {
        return Optional.ofNullable(repository.get(id));
    }

    public static Collection<Entity> getAll() {
        return repository.values();
    }

    public static void update(int id, Entity newEntity) {
        repository.put(id, newEntity);
    }
}

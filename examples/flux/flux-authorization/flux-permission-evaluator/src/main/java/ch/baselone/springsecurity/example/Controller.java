package ch.baselone.springsecurity.example;

import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping
public class Controller {
    @GetMapping("/entities")
    @PostFilter("hasPermission(filterObject,T(ch.baselone.springsecurity.example.Permission).READ)")
    public Mono<List<Entity>> readAll() {
        return Entity.getAll().collectList();
    }

    @GetMapping("/entities/{id}")
    @PreAuthorize("hasPermission(#id,'Entity',T(ch.baselone.springsecurity.example.Permission).READ)")
    public Mono<Entity> readSingle(@PathVariable("id") int id) {
        return Entity.getById(id);
    }

    @PostMapping("/entities/{id}")
    @PutMapping("/entities/{id}")
    @PreAuthorize("hasPermission(#id,'Entity',T(ch.baselone.springsecurity.example.Permission).WRITE)")
    public Mono<Void> update(@PathVariable("id") int id, @RequestBody Entity entity) {
        return Entity.update(id, entity);
    }
}

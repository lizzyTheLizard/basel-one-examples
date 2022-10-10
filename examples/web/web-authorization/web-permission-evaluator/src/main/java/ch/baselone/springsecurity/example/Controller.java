package ch.baselone.springsecurity.example;

import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedList;
import java.util.List;

@RestController
@RequestMapping
public class Controller {
    @GetMapping("/entities")
    @PostFilter("hasPermission(filterObject,T(ch.baselone.springsecurity.example.Permission).READ)")
    public List<Entity> readAll() {
        //Result must be a modifiable list, otherwise it cannot be filtered
        return new LinkedList<>(Entity.getAll());
    }

    @GetMapping("/entities/{id}")
    @PreAuthorize("hasPermission(#id,'Entity',T(ch.baselone.springsecurity.example.Permission).READ)")
    public Entity readSingle(@PathVariable("id") int id) {
        return Entity.getById(id).orElseThrow();
    }

    @PostMapping("/entities/{id}")
    @PutMapping("/entities/{id}")
    @PreAuthorize("hasPermission(#id,'Entity',T(ch.baselone.springsecurity.example.Permission).WRITE)")
    public void update(@PathVariable("id") int id, @RequestBody Entity entity) {
        Entity.update(id, entity);
    }
}

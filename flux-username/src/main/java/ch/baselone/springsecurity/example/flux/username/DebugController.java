package ch.baselone.springsecurity.example.flux.username;

import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.WebFilterChainProxy;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Stream;

@RestController
@RequestMapping("debug")
public class DebugController {
    private final WebFilterChainProxy springSecurityFilterChain;

    public DebugController(WebFilterChainProxy springSecurityFilterChain) {
        this.springSecurityFilterChain = springSecurityFilterChain;
    }

    @GetMapping("filters")
    public Mono<String> getFilterInformation() {
        return Flux.fromStream(getFilters(springSecurityFilterChain))
                .flatMap(this::getFilterChainInformation)
                .collect(StringBuilder::new, StringBuilder::append)
                .map(filterList -> new StringBuilder().append("<html><head></head><body><ul>").append(filterList).append("</ul></body></html>"))
                .map(StringBuilder::toString);
    }

    private Mono<StringBuilder> getFilterChainInformation(SecurityWebFilterChain chain) {
        return chain.getWebFilters()
                .map(this::getClassLink)
                .map(n -> new StringBuilder().append("<li>").append(n).append("</li>"))
                .collect(StringBuilder::new, StringBuilder::append)
                .map(filterList -> new StringBuilder().append("<li>").append(getClassLink(chain)).append("<ul>").append(filterList).append("</ul></li>"));
    }

    private Stream<SecurityWebFilterChain> getFilters(WebFilterChainProxy webFilterChainProxy) {
        //This is horrible... But as filters is private and there is no way to get it we have to hack arround
        try {
            Field filterField = WebFilterChainProxy.class.getDeclaredField("filters");
            filterField.setAccessible(true);
            //noinspection unchecked
            return ((List<SecurityWebFilterChain>) filterField.get(webFilterChainProxy)).stream();
        } catch (Exception e) {
            throw new RuntimeException("Cannot get filter list");
        }
    }

    private String getClassLink(Object obj) {
        String simpleName = obj.getClass().getSimpleName();
        int hashCode = obj.hashCode();
        String className = obj.getClass().getCanonicalName();
        if (className.startsWith("org.springframework.security")) {
            className = className.replace(".", "/");
            return String.format("<a href=\"https://docs.spring.io/spring-security/site/docs/5.7.0-RC1/api/%s.html\">%s (%s)</a>", className, simpleName, hashCode);
        } else {
            return String.format("%s (%s)", className, hashCode);
        }
    }
}

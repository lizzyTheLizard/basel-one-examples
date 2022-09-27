package ch.baselone.springsecurity.example.web.simple;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.Filter;
import java.security.Principal;
import java.util.Optional;
import java.util.stream.Stream;

@RestController
@RequestMapping
public class Controller {
    private final FilterChainProxy springSecurityFilterChain;

    public Controller(Filter springSecurityFilterChain) {
        this.springSecurityFilterChain = (FilterChainProxy) springSecurityFilterChain;
    }

    @RequestMapping("hello")
    public String hello() {
        Optional<Authentication> authentication = Optional.ofNullable(SecurityContextHolder.getContext()
                .getAuthentication());
        return authentication
                .map(Principal::getName)
                .map(username -> "Hello " + username)
                .orElse("Hello unauthenticated");
    }

    @RequestMapping("filters")
    public String getFilterInformation() {
        String filterList = getFilters(springSecurityFilterChain)
                .map(this::getFilterChainInformation)
                .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
                .toString();
        return "<html><head></head><body><h1>The following filter chains are in use</h1><ul>" +
                filterList +
                "</ul></body></html>";
    }

    private String getFilterChainInformation(SecurityFilterChain chain) {
        String filterList = chain.getFilters().stream()
                .map(this::getClassLink)
                .map(n -> new StringBuilder().append("<li>").append(n).append("</li>"))
                .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
                .toString();
        return "<li>" + getClassLink(chain) + "<ul>" + filterList + "</ul></li>";
    }

    private Stream<SecurityFilterChain> getFilters(FilterChainProxy filterChainProxy) {
        return filterChainProxy.getFilterChains().stream();
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

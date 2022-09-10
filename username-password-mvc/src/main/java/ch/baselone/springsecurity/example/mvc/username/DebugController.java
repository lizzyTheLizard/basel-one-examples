package ch.baselone.springsecurity.example.mvc.username;

import org.springframework.security.web.FilterChainProxy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.Filter;

@RestController
@RequestMapping("debug")
public class DebugController {
    private final Filter springSecurityFilterChain;

    public DebugController(Filter springSecurityFilterChain) {
        this.springSecurityFilterChain = springSecurityFilterChain;
    }

    @GetMapping("filters")
    public String getFilterInformation() {
        StringBuilder sb = new StringBuilder();
        sb.append("<html><head></head><body><ul>");
        FilterChainProxy filterChainProxy = (FilterChainProxy) springSecurityFilterChain;
        for (SecurityFilterChain chain : filterChainProxy.getFilterChains()) {
            sb
                    .append("<li>")
                    .append(getClassLink(chain))
                    .append("<ul>");
            for (Filter filter : chain.getFilters()) {
                sb
                        .append("<li>")
                        .append(getClassLink(filter))
                        .append("</li>");
            }
            sb.append("</ul>")
                    .append("</li>");

        }
        return sb.toString();
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

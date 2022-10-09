package ch.baselone.springsecurity.example;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.SecurityFilterChain;

import javax.servlet.Filter;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Create your own simple security chain. It will do nothing
 */
@Configuration
public class CustomSecurityChain implements SecurityFilterChain {
    @Override
    public boolean matches(HttpServletRequest request) {
        //Match all requests
        return true;
    }

    @Override
    public List<Filter> getFilters() {
        //Do nothing
        return List.of();
    }
}

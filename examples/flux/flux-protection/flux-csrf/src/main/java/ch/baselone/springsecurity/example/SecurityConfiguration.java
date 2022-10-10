package ch.baselone.springsecurity.example;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.util.matcher.PathPatternParserServerWebExchangeMatcher;

@Configuration
public class SecurityConfiguration {
    @Bean
    public SecurityWebFilterChain protectionConfiguration(ServerHttpSecurity http) {
        http.securityMatcher(new PathPatternParserServerWebExchangeMatcher("/**"));
        http.authorizeExchange(c -> c.anyExchange().permitAll());
        /*
        Cross-Site-Request-Forgery, prevents unauthorized commands from the user.
        Basically, for each POST-Form a 2nd input (the CSRF-Token) is expected and checked.
        You can define what to do if CSRF failed (accessDeniedHandler, by default 403),
        where to store the token (csrfTokenRepository, by default in the session)
        and when to use it (requireCsrfProtectionMatcher, usually POST, PUT, DELETE)
        or disable it (disable)
         */
        http.csrf(Customizer.withDefaults());
        return http.build();
    }
}

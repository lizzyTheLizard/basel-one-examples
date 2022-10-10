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
        http.headers(Customizer.withDefaults());
        return http.build();
    }
}

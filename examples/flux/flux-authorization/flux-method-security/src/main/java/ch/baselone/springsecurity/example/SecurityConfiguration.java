package ch.baselone.springsecurity.example;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.util.matcher.PathPatternParserServerWebExchangeMatcher;

@Configuration
@EnableReactiveMethodSecurity
public class SecurityConfiguration {
    @Bean
    public SecurityWebFilterChain usernameSecurityConfiguration(ServerHttpSecurity http) {
        http.securityMatcher(new PathPatternParserServerWebExchangeMatcher("/**"));
        http.formLogin(Customizer.withDefaults());
        http.authorizeExchange((authorize) -> authorize.anyExchange().permitAll());
        return http.build();
    }
}

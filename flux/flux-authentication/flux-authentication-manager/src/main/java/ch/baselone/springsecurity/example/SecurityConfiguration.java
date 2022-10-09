package ch.baselone.springsecurity.example;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.util.matcher.PathPatternParserServerWebExchangeMatcher;

@Configuration
@EnableMethodSecurity
public class SecurityConfiguration {
    @Bean
    public SecurityWebFilterChain usernameSecurityConfiguration(ServerHttpSecurity http) {
        http.securityMatcher(new PathPatternParserServerWebExchangeMatcher("/**"));
        http.formLogin(Customizer.withDefaults());
        http.authorizeExchange(c -> c.anyExchange().authenticated());
        return http.build();
    }

    //Use a custom authentication manager. This manager does basically the same as the "normal"
    //authentication manager but adds a prefix
    @Bean
    public ReactiveAuthenticationManager authenticationProvider() {
        return new CustomAuthenticationManager();
    }
}

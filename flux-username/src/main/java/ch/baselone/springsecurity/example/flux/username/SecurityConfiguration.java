package ch.baselone.springsecurity.example.flux.username;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.util.matcher.OrServerWebExchangeMatcher;
import org.springframework.security.web.server.util.matcher.PathPatternParserServerWebExchangeMatcher;

@Configuration
@EnableReactiveMethodSecurity
public class SecurityConfiguration {

    //Username-Password Login for certain pages
    @Bean
    @Order(0)
    public SecurityWebFilterChain usernameSecurityConfiguration(ServerHttpSecurity http) {
        http.securityMatcher(new OrServerWebExchangeMatcher(
                new PathPatternParserServerWebExchangeMatcher("/hello/**"),
                new PathPatternParserServerWebExchangeMatcher("/login")
        ));
        http.formLogin(Customizer.withDefaults());
        http.authorizeExchange(c -> c.anyExchange().authenticated());
        return http.build();
    }

    //In-Memory username details service, usually you want to have something else here...
    @Bean
    public MapReactiveUserDetailsService usernameUserDetails() {
        //Password is "password"
        UserDetails user = User.builder()
                .username("user")
                .password("{bcrypt}$2a$10$GRLdNijSQMUvl/au9ofL.eDwmoohzzS7.rmNSJZ.0FxO/BTk76klW")
                .roles("USER")
                .build();
        UserDetails admin = User.builder()
                .username("admin")
                .password("{bcrypt}$2a$10$GRLdNijSQMUvl/au9ofL.eDwmoohzzS7.rmNSJZ.0FxO/BTk76klW")
                .roles("USER", "ADMIN")
                .build();
        return new MapReactiveUserDetailsService(user, admin);
    }

    //Public Access for certain pages
    @Bean
    @Order(0)
    public SecurityWebFilterChain debugConfiguration(ServerHttpSecurity http) {
        http.securityMatcher(new PathPatternParserServerWebExchangeMatcher("/debug/**"));
        http.authorizeExchange(c -> c.anyExchange().permitAll());
        return http.build();
    }

    //deny all fallback
    @Bean
    public SecurityWebFilterChain defaultFilter(ServerHttpSecurity http) {
        http.securityMatcher(new PathPatternParserServerWebExchangeMatcher("/**"));
        http.authorizeExchange(c -> c.anyExchange().denyAll());
        return http.build();
    }
}

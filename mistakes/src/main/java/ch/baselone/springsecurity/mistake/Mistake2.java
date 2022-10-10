package ch.baselone.springsecurity.mistake;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

//@Configuration
public class Mistake2 {
    @Bean
    @Order(Ordered.LOWEST_PRECEDENCE)
    //Default is public
    public SecurityFilterChain fallback(HttpSecurity http) throws Exception {
        http.antMatcher("/**");
        http.authorizeHttpRequests(c -> c.anyRequest().permitAll());
        return http.build();
    }

    @Bean
    //Internal pages require login with username / password
    public SecurityFilterChain internal(HttpSecurity http) throws Exception {
        http.antMatcher("/internal/**");
        http.formLogin(Customizer.withDefaults());
        http.authorizeHttpRequests(c -> c.anyRequest().authenticated());
        return http.build();
    }

}

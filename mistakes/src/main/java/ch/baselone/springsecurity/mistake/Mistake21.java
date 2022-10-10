package ch.baselone.springsecurity.mistake;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

//@Configuration
public class Mistake21 {
    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE+100)
    //Some Pages are public
    public SecurityFilterChain internal(HttpSecurity http) throws Exception {
        return http
                .antMatcher("/test")
                .authorizeHttpRequests().antMatchers("/test").permitAll().and()
                .build();
    }

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE+100)
    //Internal requires login
    public SecurityFilterChain intern(HttpSecurity http) throws Exception {
        return http
                .antMatcher("/internal/**")
                .authorizeHttpRequests()
                    .antMatchers("/internal/base").authenticated()
                    .antMatchers("/internal/secret").hasRole("ADMIN").and()
                .formLogin().and()
                .build();
    }

    @Bean
    @Order(Ordered.LOWEST_PRECEDENCE-200)
    //Default is deny
    public SecurityFilterChain deny(HttpSecurity http) throws Exception {
        return http
                .antMatcher("/**")
                .authorizeHttpRequests().anyRequest().denyAll().and()
                .build();
    }
}

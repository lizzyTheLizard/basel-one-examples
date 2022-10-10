package ch.baselone.springsecurity.mistake;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

//@Configuration
public class Mistake11 {
    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE+100)
    //Some Pages are public
    public SecurityFilterChain internal(HttpSecurity http) throws Exception {
        return http.authorizeHttpRequests()
                .antMatchers("/test")
                .permitAll().and()
                .build();
    }

    @Bean
    @Order(Ordered.LOWEST_PRECEDENCE-100)
    //Default requires login with username / password
    public SecurityFilterChain fallback(HttpSecurity http) throws Exception {
        return http.authorizeHttpRequests()
                .antMatchers("/**")
                .authenticated().and()
                .formLogin().and()
                .build();
    }
}

package ch.baselone.springsecurity.example;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
public class SecurityConfiguration {
    @Bean
    public SecurityFilterChain usernameSecurityConfiguration(HttpSecurity http) throws Exception {
        http.antMatcher("/**");
        http.formLogin(Customizer.withDefaults());
        http.authorizeHttpRequests(c -> c.anyRequest().authenticated());
        //Use a custom authentication provider. This provider does basically the same as the "normal"
        //authentication provider but adds a prefix
        http.authenticationManager(new CustomAuthenticationManager());
        return http.build();
    }
}

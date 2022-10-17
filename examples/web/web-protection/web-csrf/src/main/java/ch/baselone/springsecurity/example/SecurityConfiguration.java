package ch.baselone.springsecurity.example;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
    @Bean
    public SecurityFilterChain usernameSecurityConfiguration(HttpSecurity http) throws Exception {
        http.antMatcher("/**");
        http.authorizeRequests(c -> c.anyRequest().permitAll());
        /*
        Cross-Site-Request-Forgery, prevents unauthorized commands from the user.
        Basically, for each POST-Form a 2nd input (the CSRF-Token) is expected and checked.
        You can define where to store the token (csrfTokenRepository, by default in the session)
        and when to use it (requireCsrfProtectionMatcher, usually POST, PUT, DELETE. Can be fine-tuned by
        ignoringAntMatchers and ignoringRequestMatchers), what to do after a login
        (sessionAuthenticationStrategy, default overwrite token, normally this should not be changed)
        or disable it (disable)
         */
        http.csrf(Customizer.withDefaults());
        return http.build();
    }
}

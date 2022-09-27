package ch.baselone.springsecurity.example.web.protection;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
    @Bean
    public SecurityFilterChain usernameSecurityConfiguration(HttpSecurity http) throws Exception {
        http.antMatcher("/**");
        http.authorizeRequests(c -> c.anyRequest().permitAll());

        /*
        Cross-Origin Resource Sharing, prevents request from other origins. By default,
        spring is using a CorsConfigurationSource-Bean, but you can also disable
        CORS for a filter-chain or define a specific configuration source
        http.cors(c -> c.disable());
        http.cors(c -> c.configurationSource(corsConfigurationSource()));
         */
        http.cors(Customizer.withDefaults());


        /*
        Cross-Site-Request-Forgery, prevents unauthorized commands from the user.
        Basically, for each POST-Form a 2nd input (the CSRF-Token) is expected and checked.
        You can define what to do if CSRF failed (accessDeniedHandler, by default 403),
        where to store the token (csrfTokenRepository, by default in the session)
        and when to use it (requireCsrfProtectionMatcher, usually POST, PUT, DELETE)
        or disable it (disable)
         */
        http.csrf(Customizer.withDefaults());

        /*
        Add security headers like X-frame-options or strict-transport-security. All of them
        can be customized
         */
        http.headers(Customizer.withDefaults());

        /*
        When to redirect from http to https, typically all request but this can be changed
        Do not enable it here as we want to test this application at localhost
        http.redirectToHttps(Customizer.withDefaults());
         */
        return http.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("https://example.com"));
        configuration.setAllowedMethods(List.of("GET", "POST"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}

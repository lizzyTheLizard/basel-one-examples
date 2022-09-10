package ch.baselone.springsecurity.example.mvc.username;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration {

    //Username-Password Login for certain pages
    @Bean
    @Order(0)
    public SecurityFilterChain usernameSecurityConfiguration(HttpSecurity http) throws Exception {
        http.requestMatchers(c -> c.antMatchers("/hello/**", "/login"));
        http.formLogin(Customizer.withDefaults());
        http.authorizeRequests(c -> c.anyRequest().authenticated());
        return http.build();
    }

    //In-Memory username details service, usually you want to have something else here...
    @Bean
    public UserDetailsService usernameUserDetails() {
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
        return new InMemoryUserDetailsManager(user, admin);
    }

    //Public Access for certain pages
    @Bean
    @Order(0)
    public SecurityFilterChain debugConfiguration(HttpSecurity http) throws Exception {
        http.requestMatchers(c -> c.antMatchers("/debug/**"));
        http.authorizeRequests(c -> c.anyRequest().permitAll());
        return http.build();
    }

    //deny all fallback
    @Bean
    public SecurityFilterChain defaultFilter(HttpSecurity http) throws Exception {
        http.requestMatchers(c -> c.antMatchers("/**"));
        http.authorizeRequests(c -> c.anyRequest().denyAll());
        return http.build();
    }
}

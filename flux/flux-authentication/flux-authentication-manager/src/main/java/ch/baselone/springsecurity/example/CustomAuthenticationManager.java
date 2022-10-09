package ch.baselone.springsecurity.example;

import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import reactor.core.publisher.Mono;

public class CustomAuthenticationManager implements ReactiveAuthenticationManager {
    private final ReactiveAuthenticationManager parent;

    public CustomAuthenticationManager() {
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
        ReactiveUserDetailsService inMemoryUserDetailsService = new MapReactiveUserDetailsService(user, admin);
        parent = new UserDetailsRepositoryReactiveAuthenticationManager(inMemoryUserDetailsService);
    }

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) throws AuthenticationException {
        return parent.authenticate(authentication)
                .map(a -> UsernamePasswordAuthenticationToken.authenticated("CUSTOM_" + a.getName(), null, a.getAuthorities()));
    }
}

package ch.baselone.springsecurity.example;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

public class CustomAuthenticationManager implements AuthenticationManager {
    private final DaoAuthenticationProvider parent;

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
        UserDetailsService inMemoryUserDetailsService = new InMemoryUserDetailsManager(user, admin);
        parent = new DaoAuthenticationProvider();
        parent.setUserDetailsService(inMemoryUserDetailsService);
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        Authentication a = parent.authenticate(authentication);
        return UsernamePasswordAuthenticationToken.authenticated("CUSTOM_" + a.getName(), null, a.getAuthorities());
    }
}

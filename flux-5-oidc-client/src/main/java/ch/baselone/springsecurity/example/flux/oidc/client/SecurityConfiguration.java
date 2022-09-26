package ch.baselone.springsecurity.example.flux.oidc.client;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.client.web.server.OAuth2AuthorizationRequestRedirectWebFilter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.util.matcher.PathPatternParserServerWebExchangeMatcher;

@Configuration
@EnableReactiveMethodSecurity
public class SecurityConfiguration {
    //Public Access for certain pages
    @Bean
    @Order(0)
    public SecurityWebFilterChain debugConfiguration(ServerHttpSecurity http) {
        http.securityMatcher(new PathPatternParserServerWebExchangeMatcher("/filters"));
        http.authorizeExchange(c -> c.anyExchange().permitAll());
        return http.build();
    }

    //OIDC-Login for the rest
    @Bean
    public SecurityWebFilterChain usernameSecurityConfiguration(ServerHttpSecurity http) {
        http.securityMatcher(new PathPatternParserServerWebExchangeMatcher("/**"));
        /*
        Configures the oauth2 login as a client. This adds two filter to the chain:
        * The OAuth2AuthorizationRequestRedirectWebFilter checks if an oauth2 login shall be started. By default, this
          is the case if a request comes to /oauth2/authorization/{clientID}. The filter then generates an
          Authentication-Requests and redirects you to the authentication server. You can configure this filter by
          setting an authorizationRequestResolver (if you want to customize the whole behavior when and how to
          generate an authenticationRequest OR a clientRegistrationRepository) and an authorizationRequestRepository
          (if you want to change how the authentication request is stored, usually this should not be changed).
         * The OAuth2LoginAuthenticationWebFilter checks if an authentication is needed and if so starts it by
           redirecting to /oauth2/authorization/{clientID}
         Additionally, it sets the default entry points (e.g. what to do if the user lacks an authentication). This



			AuthenticationWebFilter authenticationFilter = new OAuth2LoginAuthenticationWebFilter(
			authenticationManager, authorizedClientRepository, authenticationMatcher,
			authenticationConverter (clientRegistrationRepository), authenticationSuccessHandler, authenticationFailureHandler, securityContextRepository






        * how authentication find and configure authentication servers using clientRegistrationRepository,
            authorizedClientService and authorizedClientRepository. By default, those are read directly from the
            configuration properties and usually this should not be changed. But if you have more complex setups
            (e.g. a dynamic set of authentication servers, or dynamic client credentials) you can do this here.
        * what happens after the authentication using authenticationSuccessHandler, authenticationFailureHandler.
            Can be changed if you have special requirements
        * how authentication requests are handled using




        authenticationMatcher, authorizationRequestRepository,
            authenticationManager, authenticationConverter and authorizationRequestResolver.
            Usually this should not be changed.
        * how the security context is stored using securityContextRepository. Usually this should not be changed
        */
        http.oauth2Login(Customizer.withDefaults());
        http.authorizeExchange(c -> c.anyExchange().authenticated());
        return http.build();
    }
}

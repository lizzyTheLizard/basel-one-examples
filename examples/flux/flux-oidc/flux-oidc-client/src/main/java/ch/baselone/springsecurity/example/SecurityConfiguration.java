package ch.baselone.springsecurity.example;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.util.matcher.PathPatternParserServerWebExchangeMatcher;

@Configuration
@EnableMethodSecurity
public class SecurityConfiguration {
    //OIDC-Login
    @Bean
    public SecurityWebFilterChain oidcSecurityConfiguration(ServerHttpSecurity http) {
        http.securityMatcher(new PathPatternParserServerWebExchangeMatcher("/**"));
        /*
        Configures the oauth2 login as a client. This adds two filter to the chain:
        * The OAuth2AuthorizationRequestRedirectWebFilter starts recognize a request to
          /oauth2/authorization/{clientID}, generates an Authentication-Requests and redirects you
          to the authentication server. You can configure this filter by setting an authorizationRequestResolver
          (if you want to customize the whole behavior when and how to generate an authenticationRequest, usually this
          should not be changed) OR a clientRegistrationRepository (configuration of available authentication servers,
          by default load them from the properties) and an authorizationRequestRepository (if you want to change how
          the authentication request is stored, usually this should not be changed).
        * The OAuth2LoginAuthenticationWebFilter checks if there is an authentication response and if so handles it.
          You can configure this filter by setting an authenticationManager (how the authentication is generated from
          the oauth response, usually this should not be changed. However, you can also just define a bean of type
          GrantedAuthoritiesMapper, ReactiveOAuth2UserService<OidcUserRequest, OidcUser> or
          ReactiveJwtDecoderFactory<ClientRegistration> to fine tune its behavior), an authenticationConverter
          (how to convert the response into an authentication, usually this should not be changed),
          an authenticationMatcher (how to determine if this is an authentication response, by default when the URL is
          /login/oauth2/code/{registrationId}) authenticationSuccess- or an authenticationFailureHandler
          (what shall be done after an authentication, by default redirect to "/" after a success,
          redirect to /login?error after an error).
        Additionally, it sets the default entry points (e.g. what to do if the user lacks an authentication) to
        /oauth2/authorization/{clientID} so that it triggers the OAuth2AuthorizationRequestRedirectWebFilter
        */
        http.oauth2Login(Customizer.withDefaults());
        http.authorizeExchange(c -> c.anyExchange().authenticated());
        return http.build();
    }
}

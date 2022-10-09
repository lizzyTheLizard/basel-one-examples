package ch.baselone.springsecurity.example;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
        Configures the server as oauth2 resource. This adds an BearerTokenAuthenticationFilter to the chain. It
        searches a Token in the Request (usually DefaultBearerTokenResolver, can be customized by setting another
        bearerTokenResolver) and authenticates this token. It therefore registers an AuthenticationProvider using a
        JWT or opaqueToken configuration. If both configured JWT is used. The JWT-Provider is autoconfigured from the
        properties spring.security.oauth2.resourceserver.jwt, the opaque token using
        spring.security.oauth2.resourceserver.opaquetoken but both can be overwritten.

        Additionally, you can set an authenticationEntryPoint (default BearerTokenAuthenticationEntryPoint, 401 with
        error message in WWW-Authenticate-Header) and an accessDeniedHandler (BearerTokenAccessDeniedHandler, 403 with
        error message in WWW-Authenticate-Header)
        */
        http.oauth2ResourceServer(ServerHttpSecurity.OAuth2ResourceServerSpec::jwt);
        http.authorizeExchange(c -> c.anyExchange().authenticated());
        return http.build();
    }
}

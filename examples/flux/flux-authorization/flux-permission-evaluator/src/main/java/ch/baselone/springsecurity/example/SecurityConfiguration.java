package ch.baselone.springsecurity.example;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.util.matcher.PathPatternParserServerWebExchangeMatcher;

@Configuration
@EnableReactiveMethodSecurity
public class SecurityConfiguration {
    @Bean
    public SecurityWebFilterChain usernameSecurityConfiguration(ServerHttpSecurity http, DefaultMethodSecurityExpressionHandler defaultWebSecurityExpressionHandler) {
        //defaultWebSecurityExpressionHandler.setPermissionEvaluator(new CustomPermissionEvaluator());
        http.securityMatcher(new PathPatternParserServerWebExchangeMatcher("/**"));
        http.formLogin(Customizer.withDefaults());
        http.authorizeExchange((authorize) -> authorize.anyExchange().permitAll());
        return http.build();
    }


    @Bean
    @Primary
    public MethodSecurityExpressionHandler customMethodSecurityExpressionHandler() {
        DefaultMethodSecurityExpressionHandler expressionHandler = new DefaultMethodSecurityExpressionHandler();
        expressionHandler.setPermissionEvaluator(new CustomPermissionEvaluator());
        return expressionHandler;
    }

    @Bean
    public PermissionEvaluator permissionEvaluator(){
        return new CustomPermissionEvaluator();
    }
}

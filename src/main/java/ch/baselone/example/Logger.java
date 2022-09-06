package ch.baselone.example;

import javax.annotation.PostConstruct;
import javax.servlet.Filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.stereotype.Service;

@Service
public class Logger {

    @Autowired
    @Qualifier("springSecurityFilterChain")
    private Filter springSecurityFilterChain;

    @PostConstruct
    private void logAllFilters(){
        FilterChainProxy filterChainProxy = (FilterChainProxy) springSecurityFilterChain;
        for(SecurityFilterChain chain : filterChainProxy.getFilterChains()) {
            System.out.println("Chain " + chain.getClass().getCanonicalName() + ":" + chain.hashCode() );
            DefaultSecurityFilterChain defaultChain = (DefaultSecurityFilterChain) chain;
            System.out.println(defaultChain.getRequestMatcher());
            for(Filter filter : chain.getFilters()) {
                System.out.println("Filter " + filter.getClass().getCanonicalName() + ":" + filter.hashCode() );
            }
        }
    }
}

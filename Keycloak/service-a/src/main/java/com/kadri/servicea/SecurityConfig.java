package com.kadri.servicea;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

import java.util.ArrayList;
import java.util.Collection;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        grantedAuthoritiesConverter.setAuthorityPrefix("ROLE_");

        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwt -> {
            var authorities = new ArrayList<Collection<GrantedAuthority>>();
            var scopes = new JwtGrantedAuthoritiesConverter();
            var scoped = scopes.convert(jwt);
            Collection<GrantedAuthority> result = new ArrayList<>();
            if(scoped != null){
                result.addAll(scoped);
            }
            var realmAccess = jwt.getClaimAsMap("realm_access");
            if(realmAccess != null && realmAccess.get("roles") instanceof Collection){
                Collection<?> roles = (Collection<?>) realmAccess.get("roles");
                for(Object r : roles){
                    result.add(new SimpleGrantedAuthority("ROLE_" + r.toString()));
                }
            }
            return result;
        });
        http.authorizeHttpRequests(auth -> auth
                .requestMatchers("/public", "/actuator/health").permitAll()
                .anyRequest().authenticated()
        )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter))
                );
        return http.build();
    }
}

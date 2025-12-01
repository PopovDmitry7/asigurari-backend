package com.example.asigurari.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // definim userul admin/admin in memorie
    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails admin = User.withUsername("admin")
                // {noop} = nu folosim encoder, parola este in clar (doar pentru demo / curs)
                .password("{noop}admin")
                .roles("ADMIN")
                .build();

        return new InMemoryUserDetailsManager(admin);
    }

    // configuram HTTP security: Basic Auth pe toate endpoint-urile
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // pentru API REST, dezactivam CSRF (altfel POST/PUT/DELETE pot da 403)
                .csrf(csrf -> csrf.disable())

                // toate request-urile trebuie sa fie autentificate
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().authenticated()
                )

                // folosim HTTP Basic (username/parola in header)
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }
}

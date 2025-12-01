package com.example.asigurari.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public String politaPrefix() {
        // Bean simplu, folosit in service pentru a demonstra dependency injection a adauga POL inainte de numarul la polita
        return "POL";
    }
}

package com.example.asigurari.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.example.asigurari.validation.ValidEmailStructure;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Email;

@Data
@Document("clienti")
public class Client {

    @Id
    private Long id;

    @NotBlank(message = "Numele este obligatoriu")
    private String nume;

    @NotBlank(message = "Emailul este obligatoriu")
    @Email(message = "Emailul nu este valid")
    @ValidEmailStructure
    private String email;
}

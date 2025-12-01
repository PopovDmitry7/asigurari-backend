package com.example.asigurari.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document("polite")
public class Polita {

    @Id
    private String id; // Mongo id

    private String numarPolita;   // ex: "POL1000"
    private Long clientId;        // referinta catre Client
    private String tipAsigurare;
    private Double prima;
}

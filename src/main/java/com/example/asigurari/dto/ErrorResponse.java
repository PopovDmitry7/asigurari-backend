package com.example.asigurari.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ErrorResponse {
    private int status;
    private String mesaj;
    private LocalDateTime timestamp;
    private String path;
}

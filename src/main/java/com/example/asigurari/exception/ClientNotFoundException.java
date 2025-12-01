package com.example.asigurari.exception;

public class ClientNotFoundException extends RuntimeException {

    public ClientNotFoundException(Long clientId) {
        super("Nu exista client cu id " + clientId);
    }
}

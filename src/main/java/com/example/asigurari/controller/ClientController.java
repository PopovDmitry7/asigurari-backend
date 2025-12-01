package com.example.asigurari.controller;

import com.example.asigurari.dto.ErrorResponse;
import com.example.asigurari.dto.MessageResponse;
import com.example.asigurari.model.Client;
import com.example.asigurari.service.ClientService;
import com.example.asigurari.service.PolitaService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/clienti")
public class ClientController {

    private final ClientService clientService;
    private final PolitaService politaService;

    public ClientController(ClientService clientService, PolitaService politaService) {
        this.clientService = clientService;
        this.politaService = politaService;
    }

    // GET toti clientii
    @GetMapping
    public List<Client> getAllClients() {
        return clientService.getAllClients();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getClientById(
            @PathVariable Long id,
            HttpServletRequest request) {
        return clientService.getClientById(id)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> {
                    ErrorResponse error = new ErrorResponse(
                            404,
                            "Nu exista client cu id " + id,
                            LocalDateTime.now(),
                            request.getRequestURI());
                    return ResponseEntity.status(404).body(error);
                });
    }

    // POST adauga client cu id generat automat
    @PostMapping
    public ResponseEntity<?> createClient(
            @Valid @RequestBody Client client,
            BindingResult bindingResult,
            HttpServletRequest request) {
        if (bindingResult.hasErrors()) {

            String mesaje = bindingResult.getFieldErrors().stream()
                    .map(error -> error.getDefaultMessage())
                    .collect(Collectors.joining("; "));

            ErrorResponse error = new ErrorResponse(
                    400,
                    mesaje,
                    LocalDateTime.now(),
                    request.getRequestURI());

            return ResponseEntity.badRequest().body(error);
        }

        Client saved = clientService.createClient(client);

        MessageResponse response = new MessageResponse(
                "Client creat cu succes cu id " + saved.getId());

        return ResponseEntity.ok(response);
    }

    //Update la client dupa id
    @PutMapping("/{id}")
    public ResponseEntity<?> updateClient(
            @PathVariable Long id,
            @Valid @RequestBody Client client,
            BindingResult bindingResult,
            HttpServletRequest request) {
        // 🔴 Validare nume + email
        if (bindingResult.hasErrors()) {

            String mesaje = bindingResult.getFieldErrors().stream()
                    .map(error -> error.getDefaultMessage())
                    .collect(Collectors.joining("; "));

            ErrorResponse error = new ErrorResponse(
                    400,
                    mesaje,
                    LocalDateTime.now(),
                    request.getRequestURI());

            return ResponseEntity.badRequest().body(error);
        }

        Client updated = clientService.updateClient(id, client);

        // 🔴 Clientul nu exista
        if (updated == null) {
            ErrorResponse error = new ErrorResponse(
                    404,
                    "Nu exista client cu id " + id,
                    LocalDateTime.now(),
                    request.getRequestURI());
            return ResponseEntity.status(404).body(error);
        }

        // ✅ Succes – trimitem clientul actualizat
        return ResponseEntity.ok(updated);
    }

    // DELETE client cu verificare daca are polite
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteClient(
            @PathVariable Long id,
            HttpServletRequest request) {
        boolean hasPolite = politaService.clientHasPolite(id);

        if (hasPolite) {
            // ❌ Clientul are polițe → trimitem Eroare 400 cu JSON
            ErrorResponse error = new ErrorResponse(
                    400,
                    "Clientul nu poate fi sters, are polite in baza de date",
                    LocalDateTime.now(),
                    request.getRequestURI());

            return ResponseEntity.badRequest().body(error);
        }

        // ❌ Dacă clientul nu există în DB
        if (clientService.getClientById(id).isEmpty()) {
            ErrorResponse error = new ErrorResponse(
                    404,
                    "Nu exista client cu id " + id,
                    LocalDateTime.now(),
                    request.getRequestURI());
            return ResponseEntity.status(404).body(error);
        }

        // ✅ Dacă totul e ok → succes 200 cu MessageResponse
        clientService.deleteClient(id);

        return ResponseEntity.ok(
                new MessageResponse("Clientul a fost sters cu succes"));
    }

}

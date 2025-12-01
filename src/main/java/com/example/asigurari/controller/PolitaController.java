package com.example.asigurari.controller;

import com.example.asigurari.dto.ErrorResponse;
import com.example.asigurari.dto.MessageResponse;
import com.example.asigurari.exception.ClientNotFoundException;
import com.example.asigurari.model.Polita;
import com.example.asigurari.service.PolitaService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/polite")
public class PolitaController {

    private final PolitaService politaService;

    public PolitaController(PolitaService politaService) {
        this.politaService = politaService;
    }

    // GET toate politele
    @GetMapping
    public List<Polita> getAllPolite() {
        return politaService.getAllPolite();
    }

    // GET polita dupa numar (ex: POL1000)
    @GetMapping("/numar/{numarPolita}")
    public ResponseEntity<?> getByNumar(@PathVariable String numarPolita,
            HttpServletRequest request) {
        return politaService.getPolitaByNumar(numarPolita)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> {
                    ErrorResponse error = new ErrorResponse(
                            404,
                            "Nu exista polita cu numarul " + numarPolita,
                            LocalDateTime.now(),
                            request.getRequestURI());
                    return ResponseEntity.status(404).body(error);
                });
    }

    // POST creeaza polita pentru un client
    @PostMapping
    public ResponseEntity<?> createPolita(@RequestBody Polita polita,
            HttpServletRequest request) {
        try {
            Polita saved = politaService.createPolita(polita);
            MessageResponse response = new MessageResponse(
                    "Polita a fost creata cu succes cu numarul " + saved.getNumarPolita());
            return ResponseEntity.ok(response);

        } catch (ClientNotFoundException ex) {
            // 🔴 clientId gresit -> 400 cu ErrorResponse
            ErrorResponse error = new ErrorResponse(
                    400,
                    ex.getMessage(),
                    LocalDateTime.now(),
                    request.getRequestURI());
            return ResponseEntity.badRequest().body(error);

        } catch (IllegalArgumentException ex) {
            // ex: clientId null sau alte argumente invalide
            ErrorResponse error = new ErrorResponse(
                    400,
                    ex.getMessage(),
                    LocalDateTime.now(),
                    request.getRequestURI());
            return ResponseEntity.badRequest().body(error);
        }
    }

    // Update polita dupa ID
    @PutMapping("/numar/{numarPolita}")
    public ResponseEntity<?> updatePolita(@PathVariable String numarPolita,
            @RequestBody Polita polita,
            HttpServletRequest request) {

        try {
            Polita updated = politaService.updatePolitaByNumar(numarPolita, polita);

            // ❌ dacă polita nu există
            if (updated == null) {
                ErrorResponse error = new ErrorResponse(
                        404,
                        "Nu exista polita cu numarul " + numarPolita,
                        LocalDateTime.now(),
                        request.getRequestURI());
                return ResponseEntity.status(404).body(error);
            }

            // ✅ succes
            return ResponseEntity.ok(updated);

        } catch (ClientNotFoundException ex) {

            ErrorResponse error = new ErrorResponse(
                    400,
                    ex.getMessage(),
                    LocalDateTime.now(),
                    request.getRequestURI());
            return ResponseEntity.badRequest().body(error);

        } catch (IllegalArgumentException ex) {

            ErrorResponse error = new ErrorResponse(
                    400,
                    ex.getMessage(),
                    LocalDateTime.now(),
                    request.getRequestURI());
            return ResponseEntity.badRequest().body(error);
        }
    }

    // DELETE polita dupa numar
    @DeleteMapping("/numar/{numarPolita}")
    public ResponseEntity<?> deleteByNumar(@PathVariable String numarPolita,
            HttpServletRequest request) {

        var politaOpt = politaService.getPolitaByNumar(numarPolita);

        if (politaOpt.isEmpty()) {
            ErrorResponse error = new ErrorResponse(
                    404,
                    "Nu exista polita cu numarul " + numarPolita,
                    LocalDateTime.now(),
                    request.getRequestURI());
            return ResponseEntity.status(404).body(error);
        }

        politaService.deletePolitaByNumar(numarPolita);

        return ResponseEntity.ok(
                new MessageResponse("Polita a fost stearsa cu succes"));
    }
}

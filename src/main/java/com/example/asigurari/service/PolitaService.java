package com.example.asigurari.service;

import com.example.asigurari.component.NotificareComponent;
import com.example.asigurari.model.Polita;
import com.example.asigurari.repository.ClientRepository;
import com.example.asigurari.repository.PolitaRepository;
import com.example.asigurari.exception.ClientNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PolitaService {

    private final PolitaRepository politaRepository;
    private final SequenceService sequenceService;
    private final NotificareComponent notificareComponent;
    private final String politaPrefix;
    private final ClientRepository clientRepository;

    public PolitaService(PolitaRepository politaRepository,
            SequenceService sequenceService,
            NotificareComponent notificareComponent,
            String politaPrefix,
            ClientRepository clientRepository) {
        this.politaRepository = politaRepository;
        this.sequenceService = sequenceService;
        this.notificareComponent = notificareComponent;
        this.politaPrefix = politaPrefix;
        this.clientRepository = clientRepository;
    }

    public List<Polita> getAllPolite() {
        return politaRepository.findAll();
    }

    public Optional<Polita> getPolitaByNumar(String numarPolita) {
        return politaRepository.findByNumarPolita(numarPolita);
    }

    public Polita createPolita(Polita polita) {
        if (polita.getClientId() == null) {
            throw new IllegalArgumentException("ClientId este obligatoriu pentru a crea o polita");
        }

        boolean clientExists = clientRepository.existsById(polita.getClientId());
        if (!clientExists) {
            throw new ClientNotFoundException(polita.getClientId());
        }

        long nextNumber = sequenceService.getNextValue("polita_seq", 1000);
        String numarCuPrefix = politaPrefix + nextNumber; // ex: POL1000

        polita.setNumarPolita(numarCuPrefix);

        Polita saved = politaRepository.save(polita);
        notificareComponent.trimiteNotificare(
                "Polita noua creata cu numar " + saved.getNumarPolita());

        return saved;
    }

    public void deletePolitaByNumar(String numarPolita) {
        politaRepository.deleteByNumarPolita(numarPolita);
    }

    public Polita updatePolitaByNumar(String numarPolita, Polita updated) {

        return politaRepository.findByNumarPolita(numarPolita)
                .map(existing -> {

                    // dacă clientId este schimbat -> verificăm dacă există
                    if (updated.getClientId() != null &&
                            !updated.getClientId().equals(existing.getClientId())) {

                        boolean clientExists = clientRepository.existsById(updated.getClientId());
                        if (!clientExists) {
                            throw new ClientNotFoundException(updated.getClientId());
                        }

                        existing.setClientId(updated.getClientId());
                    }

                    // actualizăm doar câmpurile modificate
                    existing.setTipAsigurare(updated.getTipAsigurare());
                    existing.setPrima(updated.getPrima());

                    // numarPolita nu se schimbă
                    return politaRepository.save(existing);
                })
                .orElse(null); // îl tratăm în controller ca 404
    }

    public boolean clientHasPolite(Long clientId) {
        return politaRepository.existsByClientId(clientId);
    }
}

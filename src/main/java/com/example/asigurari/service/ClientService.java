package com.example.asigurari.service;

import com.example.asigurari.component.NotificareComponent;
import com.example.asigurari.model.Client;
import com.example.asigurari.repository.ClientRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClientService {

    private final ClientRepository clientRepository;
    private final SequenceService sequenceService;
    private final NotificareComponent notificareComponent;

    public ClientService(ClientRepository clientRepository,
            SequenceService sequenceService,
            NotificareComponent notificareComponent) {
        this.clientRepository = clientRepository;
        this.sequenceService = sequenceService;
        this.notificareComponent = notificareComponent;
    }

    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }

    public Client createClient(Client client) {
        long nextId = sequenceService.getNextValue("client_seq", 1);
        client.setId(nextId);
        Client saved = clientRepository.save(client);
        notificareComponent.trimiteNotificare("Client creat cu id " + saved.getId());
        return saved;
    }

    public Optional<Client> getClientById(Long id) {
        return clientRepository.findById(id);
    }

    public Client updateClient(Long id, Client updated) {
        return clientRepository.findById(id)
                .map(existing -> {
                    existing.setNume(updated.getNume());
                    existing.setEmail(updated.getEmail());
                    return clientRepository.save(existing);
                })
                .orElse(null);
    }

    public void deleteClient(Long id) {
        clientRepository.deleteById(id);
    }
}

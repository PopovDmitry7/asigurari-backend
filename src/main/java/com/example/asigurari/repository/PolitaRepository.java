package com.example.asigurari.repository;

import com.example.asigurari.model.Polita;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PolitaRepository extends MongoRepository<Polita, String> {

    Optional<Polita> findByNumarPolita(String numarPolita);

    boolean existsByClientId(Long clientId);

    void deleteByNumarPolita(String numarPolita);
}

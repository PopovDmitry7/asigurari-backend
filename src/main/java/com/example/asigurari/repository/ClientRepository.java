package com.example.asigurari.repository;

import com.example.asigurari.model.Client;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends MongoRepository<Client, Long> {
    // existsById este deja oferit de MongoRepository, dar îl lăsăm pentru claritate
    boolean existsById(Long id);
}

package com.example.asigurari.service;

import com.example.asigurari.model.Sequence;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

@Service
public class SequenceService {

    private final MongoOperations mongoOperations;

    public SequenceService(MongoOperations mongoOperations) {
        this.mongoOperations = mongoOperations;
    }

    public long getNextValue(String key, long startFrom) {
        Query query = new Query(Criteria.where("_id").is(key));
        Update update = new Update().inc("value", 1);
        FindAndModifyOptions options = FindAndModifyOptions.options()
                .returnNew(true)
                .upsert(true);

        Sequence sequence = mongoOperations.findAndModify(query, update, options, Sequence.class);

        // Prima oara value va fi 1 -> o setam la startFrom (1000)
        if (sequence.getValue() == 1 && startFrom > 1) {
            sequence.setValue(startFrom);
            mongoOperations.save(sequence);
            return startFrom;
        }

        return sequence.getValue();
    }

}

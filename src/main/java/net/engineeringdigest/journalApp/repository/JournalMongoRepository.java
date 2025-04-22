package net.engineeringdigest.journalApp.repository;

import net.engineeringdigest.journalApp.entity.JournalEntry;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface JournalMongoRepository extends MongoRepository<JournalEntry,String> {
}

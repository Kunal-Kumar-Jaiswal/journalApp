package net.engineeringdigest.journalApp.service;

import net.engineeringdigest.journalApp.entity.JournalEntry;
import net.engineeringdigest.journalApp.entity.User;
import net.engineeringdigest.journalApp.repository.JournalMongoRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
public class JournalService {
    @Autowired
    private JournalMongoRepository myRepository;

    @Autowired
    private UserService userService;

    public List<JournalEntry> findAllEntry(String userName) {
        User user = userService.findByUserName(userName);
        List<JournalEntry> journalEntryList = user.getJournalEntryList();
        return journalEntryList;
        //return myRepository.findAll(user.getJournalEntryList());
    }

    public JournalEntry insertOne(JournalEntry myEntry) {
        return myRepository.save(myEntry);
    }

    @Transactional
    public JournalEntry insertOne(JournalEntry myEntry, String userName) {
        User user = userService.findByUserName(userName);
        myEntry.setDate(LocalDateTime.now());
        JournalEntry saved = myRepository.save(myEntry);
        user.getJournalEntryList().add(saved);
        userService.insertOne(user);
        return myEntry;
    }

    public Optional<JournalEntry> findByEntryId(ObjectId myId) {
        return myRepository.findById(String.valueOf(myId));
    }

    public boolean deleteByEntryId(ObjectId myId,String userName) {
        User user = userService.findByUserName(userName);
        user.getJournalEntryList().removeIf(x -> x.getId().equals(myId));
        userService.insertOne(user);
        myRepository.deleteById(String.valueOf(myId));
        return true;
    }

    //public JournalEntry updateByEntryId
}

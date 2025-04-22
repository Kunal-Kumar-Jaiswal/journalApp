package net.engineeringdigest.journalApp.controller;

import net.engineeringdigest.journalApp.entity.JournalEntry;
import net.engineeringdigest.journalApp.service.JournalService;
import net.engineeringdigest.journalApp.service.UserService;
import org.apache.coyote.Response;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/journal")
public class JournalControllerV2 {

    @Autowired
    private JournalService journalService;

    @GetMapping("/{userName}")
    public ResponseEntity<List<JournalEntry>> getAllEntriesOfUser(@PathVariable String userName) {
        List<JournalEntry> all = journalService.findAllEntry(userName);
        if(all != null && !all.isEmpty()) {
            return new ResponseEntity<>(all, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/{userName}")
    public ResponseEntity<JournalEntry> addEntry(@RequestBody JournalEntry myEntry, @PathVariable String userName) {
        try {
            journalService.insertOne(myEntry,userName);
            return new ResponseEntity<>(myEntry,HttpStatus.CREATED);
        }catch(Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("id/{myId}")
    public ResponseEntity<JournalEntry> getById(@PathVariable ObjectId myId) {
        Optional<JournalEntry> existEntry = journalService.findByEntryId(myId);
        if(existEntry.isPresent()) {
            return new ResponseEntity<>(existEntry.get(),HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("id/{userName}/{myId}")
    public ResponseEntity<?> deleteById(@PathVariable String userName,@PathVariable ObjectId myId) {
        if(!journalService.findByEntryId(myId).isPresent()) {
            return new ResponseEntity<>("Not Found",HttpStatus.NOT_FOUND);
        }
        journalService.deleteByEntryId(myId,userName);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("id/{myId}")
    public ResponseEntity<JournalEntry> updateById(@PathVariable ObjectId myId,@RequestBody JournalEntry newEntry) {
        Optional<JournalEntry> existEntry = journalService.findByEntryId(myId);
        if(existEntry.isPresent()) {
            JournalEntry getOldEntry = existEntry.get();
            getOldEntry.setTitle(!newEntry.getTitle().isEmpty() && !getOldEntry.getTitle().equals(newEntry.getTitle()) ? newEntry.getTitle() : getOldEntry.getTitle());
            getOldEntry.setContent(!newEntry.getContent().isEmpty() && !getOldEntry.getContent().equals(newEntry.getContent()) ? newEntry.getContent() : getOldEntry.getContent());
            journalService.insertOne(getOldEntry);
            return new ResponseEntity<>(getOldEntry, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}

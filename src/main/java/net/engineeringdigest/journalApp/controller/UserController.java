package net.engineeringdigest.journalApp.controller;

import net.engineeringdigest.journalApp.entity.User;
import net.engineeringdigest.journalApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> all = userService.getAllUsers();
        if(all != null && !all.isEmpty()) {
            return new ResponseEntity<>(all, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public ResponseEntity<User> addUser(@RequestBody User user) {
        try {
            userService.insertOne(user);
            return new ResponseEntity<>(user,HttpStatus.CREATED);
        }catch(Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("id/{userName}")
    public ResponseEntity<User> getUserById(@PathVariable String userName) {
        User existUser = userService.findByUserName(userName);
        if(existUser!=null) {
            return new ResponseEntity<>(existUser,HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

//    @DeleteMapping("id/{myId}")
//    public ResponseEntity<?> deleteById(@PathVariable ObjectId myId) {
//        if(!userService.findByEntryId(myId).isPresent()) {
//            return new ResponseEntity<>("Not Found",HttpStatus.NOT_FOUND);
//        }
//        userService.deleteByEntryId(myId);
//        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//    }

    @PutMapping("id/{userName}")
    public ResponseEntity<User> updateById(@PathVariable String userName,@RequestBody User user) {
        User existUser = userService.findByUserName(userName);
        if(existUser!=null) {
            existUser.setUserName(user.getUserName());
            existUser.setPassword(user.getPassword());
            userService.insertOne(existUser);
            return new ResponseEntity<>(existUser, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}

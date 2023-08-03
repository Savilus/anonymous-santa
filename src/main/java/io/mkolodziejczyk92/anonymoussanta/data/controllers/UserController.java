package io.mkolodziejczyk92.anonymoussanta.data.controllers;

import io.mkolodziejczyk92.anonymoussanta.data.model.UserDto;
import io.mkolodziejczyk92.anonymoussanta.data.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<String> createUser(@RequestBody UserDto userDto) {
        try{
            userService.saveUser(userDto);
            return ResponseEntity.ok("User has been created");
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while trying to save the event.");
        }
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers() {
        try{
            return ResponseEntity.status(HttpStatus.OK).body(userService.getAllUsers());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ArrayList<>());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateUserById(@RequestBody UserDto userDto, @PathVariable Long id) {
        try {
            userService.updateUserById(id, userDto);
            return ResponseEntity.ok("User has been updated");
        } catch (Exception e){
            return  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while trying to update the user");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUserById(@PathVariable Long id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.noContent().build();
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while trying to delete the user.");
        }
    }
}

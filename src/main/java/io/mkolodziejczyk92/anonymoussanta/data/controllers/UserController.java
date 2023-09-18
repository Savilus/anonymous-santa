package io.mkolodziejczyk92.anonymoussanta.data.controllers;

import io.mkolodziejczyk92.anonymoussanta.data.exceptions.UserNotFoundException;
import io.mkolodziejczyk92.anonymoussanta.data.model.UserDto;
import io.mkolodziejczyk92.anonymoussanta.data.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {
    private final UserService userService;


    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/profile")
    public ResponseEntity<UserDto> getUserInformation(@RequestHeader("Authorization") String bearerToken) throws UserNotFoundException {
            return ResponseEntity.ok().body(userService.getUserDtoFromToken(bearerToken));
    }

    @PostMapping("/gifts")
    public ResponseEntity<String> saveUserGiftChoices(@RequestHeader("Authorization") String bearerToken,
                                                            @RequestBody List<String> userGiftChoices) throws UserNotFoundException {

            userService.saveUserGiftChoices(bearerToken, userGiftChoices);
            return ResponseEntity.ok().body("Gifts chosen successfully!");



    }


}

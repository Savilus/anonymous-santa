package io.mkolodziejczyk92.anonymoussanta.data.service;

import io.mkolodziejczyk92.anonymoussanta.data.config.JwtService;
import io.mkolodziejczyk92.anonymoussanta.data.entity.User;
import io.mkolodziejczyk92.anonymoussanta.data.exceptions.UserNotFoundException;
import io.mkolodziejczyk92.anonymoussanta.data.model.UserDto;
import io.mkolodziejczyk92.anonymoussanta.data.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;

    private final JwtService jwtService;

    public UserService(UserRepository userRepository, JwtService jwtService) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }

    public User getUserById(String organizerId) {
        return userRepository.findById(Long.valueOf(organizerId)).orElseThrow();
    }

    public User getUserByEmail(String email){
        return userRepository.findByEmail(email).orElseThrow();
    }

    public Long getUserIdFromToken(String bearerToken) throws UserNotFoundException {
        String token = bearerToken.substring(7);
        String extractedUsername = jwtService.extractUserName(token);

        return userRepository.findByEmail(extractedUsername)
                .map(user -> user.getId())
                .orElseThrow(() -> new UserNotFoundException("User not found."));
    }

    public UserDto getUserDtoFromToken(String bearerToken) throws UserNotFoundException {
        String token = bearerToken.substring(7);
        String extractedUsername = jwtService.extractUserName(token);
        return userRepository.findByEmail(extractedUsername)
                .map(user -> UserDto.builder()
                        .email(user.getEmail())
                        .firstName(user.getFirstName())
                        .lastName(user.getLastName())
                        .build())
                .orElseThrow(() -> new UserNotFoundException("User not found."));
    }

    @Transactional
    public void saveUserGiftChoices(String bearerToken, List<String> userGiftChoices) throws UserNotFoundException {
        Long userIdFromToken = getUserIdFromToken(bearerToken);
        User user = userRepository.findById(userIdFromToken).orElseThrow(() -> new UserNotFoundException("User not found."));
        user.setPreferredGifts(userGiftChoices);
        userRepository.save(user);

    }
}

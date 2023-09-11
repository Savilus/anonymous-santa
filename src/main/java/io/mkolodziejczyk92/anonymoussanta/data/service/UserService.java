package io.mkolodziejczyk92.anonymoussanta.data.service;

import io.mkolodziejczyk92.anonymoussanta.data.config.JwtService;
import io.mkolodziejczyk92.anonymoussanta.data.entity.User;
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

    public Long getUserIdFromToken(String bearerToken) {
        String token = bearerToken.substring(7);
        String extractedUsername = jwtService.extractUserName(token);
        return userRepository.findByEmail(extractedUsername).get().getId();
    }

    public UserDto getUserDtoFromToken(String bearerToken) {
        String token = bearerToken.substring(7);
        String extractedUsername = jwtService.extractUserName(token);
        User user = userRepository.findByEmail(extractedUsername).get();

        return UserDto.builder()
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .build();
    }

    @Transactional
    public void saveUserGiftChoices(String bearerToken, List<String> userGiftChoices) {
        Long userIdFromToken = getUserIdFromToken(bearerToken);
        User user = userRepository.findById(userIdFromToken).orElseThrow(() -> new EntityNotFoundException("Event does not exist."));
        user.setPreferredGifts(userGiftChoices);
        userRepository.save(user);

    }
}

package io.mkolodziejczyk92.anonymoussanta;

import io.mkolodziejczyk92.anonymoussanta.data.config.JwtService;
import io.mkolodziejczyk92.anonymoussanta.data.entity.User;
import io.mkolodziejczyk92.anonymoussanta.data.exceptions.UserNotFoundException;
import io.mkolodziejczyk92.anonymoussanta.data.model.UserDto;
import io.mkolodziejczyk92.anonymoussanta.data.repository.UserRepository;
import io.mkolodziejczyk92.anonymoussanta.data.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtService jwtService;

    private UserService userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        userService = new UserService(userRepository, jwtService);
    }

    @Test
    public void testGetUserById() {
        long userId = 1L;
        User mockUser = new User();
        mockUser.setId(userId);


        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));

        User result = userService.getUserById(String.valueOf(userId));

        assertNotNull(result);
        assertEquals(mockUser, result);
    }

    @Test
    public void testGetUserByEmail() {
        String email = "test@example.com";
        User mockUser = new User();
        mockUser.setEmail(email);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(mockUser));

        User result = userService.getUserByEmail(email);

        assertNotNull(result);
        assertEquals(mockUser, result);
    }

    @Test
    public void testGetUserIdFromToken() throws UserNotFoundException {
        String bearerToken = "Bearer abcdefg";
        String usernameFromToken = "test@example.com";
        User mockUser = new User();
        mockUser.setEmail(usernameFromToken);
        mockUser.setId(1L);

        when(jwtService.extractUserName("abcdefg")).thenReturn(usernameFromToken);
        when(userRepository.findByEmail(usernameFromToken)).thenReturn(Optional.of(mockUser));

        Long result = userService.getUserIdFromToken(bearerToken);

        assertNotNull(result);
        assertEquals(1L, result.longValue());
    }

    @Test
    public void testGetUserDtoFromToken() throws UserNotFoundException{
        String bearerToken = "Bearer abcdefg";
        String usernameFromToken = "test@example.com";
        User mockUser = new User();
        mockUser.setEmail(usernameFromToken);
        when(jwtService.extractUserName("abcdefg")).thenReturn(usernameFromToken);
        when(userRepository.findByEmail(usernameFromToken)).thenReturn(Optional.of(mockUser));

        UserDto result = userService.getUserDtoFromToken(bearerToken);

        assertNotNull(result);
        assertEquals(usernameFromToken, result.getEmail());
    }

    @Test
    public void testSaveUserGiftChoices() throws UserNotFoundException{
        String bearerToken = "Bearer abcdefg";
        Long userId = 1L;
        List<String> giftChoices = List.of("Gift1", "Gift2");
        User mockUser = new User();
        mockUser.setId(userId);
        mockUser.setPreferredGifts(giftChoices);
        when(jwtService.extractUserName("abcdefg")).thenReturn("test@example.com");
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(mockUser));
        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));

        userService.saveUserGiftChoices(bearerToken, giftChoices);

        assertTrue(mockUser.getPreferredGifts().containsAll(giftChoices));
    }

}

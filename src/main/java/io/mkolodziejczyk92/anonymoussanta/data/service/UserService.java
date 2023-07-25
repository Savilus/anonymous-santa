package io.mkolodziejczyk92.anonymoussanta.data.service;

import io.mkolodziejczyk92.anonymoussanta.data.entity.User;
import io.mkolodziejczyk92.anonymoussanta.data.mapper.UserMapper;
import io.mkolodziejczyk92.anonymoussanta.data.model.UserDto;
import io.mkolodziejczyk92.anonymoussanta.data.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    private final UserMapper userMapper;

    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public  List<UserDto> getAllUsers() {
        return userMapper.mapToUserDtoList(userRepository.findAll());
    }
    public User saveUser(UserDto userDto) {
       return  userRepository.save(userMapper.mapToUser(userDto));
    }

    public void updateUserById(Long id, UserDto userDto) {
        // TODO: zastanowić się nad dto z polem id
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
    public User getUserById(String organizerId) {
        return userRepository.findById(Long.valueOf(organizerId)).orElseThrow();
    }
}

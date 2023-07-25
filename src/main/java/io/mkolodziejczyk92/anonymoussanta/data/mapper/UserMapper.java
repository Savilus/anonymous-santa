package io.mkolodziejczyk92.anonymoussanta.data.mapper;

import io.mkolodziejczyk92.anonymoussanta.data.entity.User;
import io.mkolodziejczyk92.anonymoussanta.data.model.UserDto;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper(componentModel = "spring")
@Component
public interface UserMapper {

    UserDto mapToUserDto(User user);

    @InheritInverseConfiguration
    User mapToUser(UserDto userDto);

    List<UserDto> mapToUserDtoList(List<User> allUser);

    @InheritInverseConfiguration
    List<User> mapToUserList(List<UserDto> allUsers);
}

package io.mkolodziejczyk92.anonymoussanta.data.model;

import io.mkolodziejczyk92.anonymoussanta.data.enums.ERole;
import lombok.*;

import java.util.List;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class UserDto {

    private Long id;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private Set<ERole> userRoles;
    private List<String> preferredGifts;
}

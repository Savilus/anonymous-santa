package io.mkolodziejczyk92.anonymoussanta.data.model;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class InvitationDto {

    private String participantName;
    private String participantSurname;
    private String participantEmail;
    private boolean participantStatus;
    private String eventPassword;
    private String giftReceiver;
    private EventDto eventDto;
    private UserDto userDto;

}

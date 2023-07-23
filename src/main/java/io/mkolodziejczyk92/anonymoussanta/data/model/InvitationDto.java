package io.mkolodziejczyk92.anonymoussanta.data.model;

import io.mkolodziejczyk92.anonymoussanta.data.entity.Event;
import io.mkolodziejczyk92.anonymoussanta.data.entity.User;
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
    private Event event;
    private User user;

}

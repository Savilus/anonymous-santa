package io.mkolodziejczyk92.anonymoussanta.data.model;

import lombok.*;

import java.util.Date;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class EventDto {

    private Long id;
    private String name;
    private Date eventDate;
    private Integer numberOfPeople;
    private Integer budget;
    private String currency;
    private String imageUrl;
    private List<String> eventPasswords;
    private String organizerId;
    private String giftReceiverForLogInUser;
    private List<InvitationDto> listOfInvitationForEvent;
    private boolean logInUserIsAnOrganizer;

}

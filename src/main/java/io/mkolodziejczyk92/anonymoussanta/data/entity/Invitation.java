package io.mkolodziejczyk92.anonymoussanta.data.entity;

import jakarta.persistence.*;
import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "invitation")
public class Invitation extends IdCreator{

    @Column(name = "participant_name")
    private String participantName;

    @Column(name = "participant_surname")
    private String participantSurname;

    @Column(name = "participant_email")
    private String participantEmail;

    @Column(name = "participant_status")
    private boolean participantStatus;

    @Column(name = "invitation_password")
    private String invitationPassword;

    @Column(name = "gift_receiver")
    private String giftReceiver;

    @ManyToOne
    private Event event;

    @ManyToOne
    private User user;

    public String getFullName(){
        return participantName + " " + participantSurname;
    }
}

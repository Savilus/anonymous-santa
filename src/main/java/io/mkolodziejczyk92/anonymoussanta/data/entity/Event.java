package io.mkolodziejczyk92.anonymoussanta.data.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "event")
public class Event extends IdCreator {

    private String name;

    @Column(name = "event_date")
    private Date eventDate;

    @Column(name = "number_of_people")
    private Integer numberOfPeople;

    private Integer budget;

    private String currency;

    private boolean afterDraw;

    @Column(name = "image_url")
    private String imageUrl;

    @OneToMany(mappedBy = "event")
    private List<Invitation> listOfInvitationForEvent;

    @ManyToOne
    private User organizer;

}

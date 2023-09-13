package io.mkolodziejczyk92.anonymoussanta;

import io.mkolodziejczyk92.anonymoussanta.data.entity.Event;
import io.mkolodziejczyk92.anonymoussanta.data.entity.Invitation;
import io.mkolodziejczyk92.anonymoussanta.data.entity.User;
import io.mkolodziejczyk92.anonymoussanta.data.model.EventDto;
import io.mkolodziejczyk92.anonymoussanta.data.repository.EventRepository;
import io.mkolodziejczyk92.anonymoussanta.data.repository.InvitationRepository;
import io.mkolodziejczyk92.anonymoussanta.data.service.EventService;
import io.mkolodziejczyk92.anonymoussanta.data.service.InvitationService;
import io.mkolodziejczyk92.anonymoussanta.data.service.MailSander;
import io.mkolodziejczyk92.anonymoussanta.data.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.mockito.Mockito.*;

public class EventServiceTest {

    @Mock
    private EventRepository eventRepository;

    @Mock
    private InvitationService invitationService;

    @Mock
    private InvitationRepository invitationRepository;

    @Mock
    private UserService userService;

    @Mock
    private MailSander mailSander;

    private EventService eventService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        eventService = new EventService(eventRepository, invitationService, invitationRepository, userService);
    }

    @Test
    public void testSaveEventAndSendInvitationsToParticipants() {
        EventDto eventDto = new EventDto();
        eventDto.setName("Test Event");

        when(eventRepository.save(any(Event.class))).thenReturn(new Event());

        when(invitationService.createListOfInvitationEntitiesForSavingEvent(anyList(), any(Event.class)))
                .thenReturn(Collections.emptyList());

        eventService.saveEventAndSendInvitationsToParticipants(eventDto);

        verify(eventRepository, times(1)).save(any(Event.class));

        verify(mailSander, times(0)).sendEmailWithInvitation(any(), any(), any(), any(), any());
    }

    @Test
    public void testDeleteEvent() {
        Long eventId = 1L;
        Long userId = 2L;
        Event event = new Event();
        User user = User.builder().build();
        user.setId(userId);
        event.setOrganizer(user);


        when(eventRepository.findById(eventId)).thenReturn(Optional.of(event));

        eventService.deleteEvent(eventId, userId);

        verify(eventRepository).deleteById(eventId);
    }


}
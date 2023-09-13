package io.mkolodziejczyk92.anonymoussanta;

import io.mkolodziejczyk92.anonymoussanta.data.entity.Event;
import io.mkolodziejczyk92.anonymoussanta.data.entity.Invitation;
import io.mkolodziejczyk92.anonymoussanta.data.model.InvitationDto;
import io.mkolodziejczyk92.anonymoussanta.data.repository.InvitationRepository;
import io.mkolodziejczyk92.anonymoussanta.data.service.InvitationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class InvitationServiceTest {
    @InjectMocks
    private InvitationService invitationService;

    @Mock
    private InvitationRepository invitationRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCreateListOfInvitationEntitiesForSavingEvent() {
        Event event = new Event();
        event.setName("Test Event");

        List<InvitationDto> invitationDtoList = new ArrayList<>();
        invitationDtoList.add(InvitationDto.builder()
                .participantName("John")
                .participantSurname("Doe")
                .participantEmail("john@example.com")
                .build());

        List<Invitation> invitationEntities = invitationService.createListOfInvitationEntitiesForSavingEvent(invitationDtoList, event);

        assertNotNull(invitationEntities);
        assertEquals(1, invitationEntities.size());
        Invitation invitation = invitationEntities.get(0);
        assertEquals("John", invitation.getParticipantName());
        assertEquals("Doe", invitation.getParticipantSurname());
        assertEquals("john@example.com", invitation.getParticipantEmail());
        assertFalse(invitation.isParticipantStatus());
        assertEquals(event, invitation.getEvent());
    }

    @Test
    public void testGetAllUserAcceptedInvitations() {
        Long userId = 1L;
        List<Invitation> invitations = new ArrayList<>();
        invitations.add(
                Invitation.builder()
                .participantStatus(true)
                .build());
        invitations.add(
                Invitation.builder()
                .participantStatus(true)
                .build());

        when(invitationRepository.findAllByParticipantStatusIsTrueAndUserId(userId)).thenReturn(invitations);

        List<Invitation> retrievedInvitations = invitationService.getAllUserAcceptedInvitations(userId);

        assertNotNull(retrievedInvitations);
        assertEquals(2, retrievedInvitations.size());
    }

    @Test
    public void testGetAllInvitationsForEvent() {
        Long eventId = 1L;
        List<Invitation> invitations = new ArrayList<>();
        invitations.add(new Invitation());
        invitations.add(new Invitation());

        when(invitationRepository.findAllByEventId(eventId)).thenReturn(invitations);

        List<Invitation> retrievedInvitations = invitationService.getAllInvitationsForEvent(eventId);

        assertNotNull(retrievedInvitations);
        assertEquals(2, retrievedInvitations.size());
    }

    @Test
    public void testAddNewInvitation() {
        Invitation invitation = new Invitation();
        invitation.setParticipantName("Jane");
        invitation.setParticipantSurname("Doe");

        when(invitationRepository.save(invitation)).thenReturn(invitation);

        Invitation savedInvitation = invitationService.addNewInvitation(invitation);

        assertNotNull(savedInvitation);
        assertEquals("Jane", savedInvitation.getParticipantName());
        assertEquals("Doe", savedInvitation.getParticipantSurname());
    }

}



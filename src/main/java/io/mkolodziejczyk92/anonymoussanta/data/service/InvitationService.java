package io.mkolodziejczyk92.anonymoussanta.data.service;

import io.mkolodziejczyk92.anonymoussanta.data.entity.Event;
import io.mkolodziejczyk92.anonymoussanta.data.entity.Invitation;
import io.mkolodziejczyk92.anonymoussanta.data.model.InvitationDto;
import io.mkolodziejczyk92.anonymoussanta.data.repository.InvitationRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class InvitationService {

    private final InvitationRepository invitationRepository;

    public InvitationService(InvitationRepository invitationRepository) {
        this.invitationRepository = invitationRepository;
    }

    public void setGiftReceiver(Long invitationId, Long receiverId){
        Optional<Invitation> invitationOptional = invitationRepository.findById(invitationId);
        invitationOptional.ifPresentOrElse(invitation -> {
            invitation.setGiftReceiver(invitationRepository.findById(receiverId).get().getFullName());
            invitationRepository.save(invitation);
        }, () -> {
            throw new EntityNotFoundException("Invitation dose not exist.");
        });
    }

    public List<Invitation> createListOfInvitationEntitiesForSavingEvent(List<InvitationDto> listOfInvitationForEvent, Event event) {
        List<Invitation> invitationEntities = new ArrayList<>();
        for (InvitationDto invitation: listOfInvitationForEvent) {
            invitationEntities.add(Invitation.builder()
                            .participantName(invitation.getParticipantName())
                            .participantSurname(invitation.getParticipantSurname())
                            .participantEmail(invitation.getParticipantEmail())
                            .participantStatus(false)
                            .eventPassword(null)
                            .event(event)
                            .user(null)
                    .build());
        }
        return invitationEntities;
    }

    public List<Invitation> getAllUserAcceptedInvitations(Long userId){
        return invitationRepository.findAllByParticipantStatusIsTrueAndUserId(userId);
    }

    public List<Invitation> getAllInvitationsForEvent(Long eventId) {
        return invitationRepository.findAllByEventId(eventId);
    }
}

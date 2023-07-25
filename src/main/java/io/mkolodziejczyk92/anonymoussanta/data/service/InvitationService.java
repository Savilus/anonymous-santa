package io.mkolodziejczyk92.anonymoussanta.data.service;

import io.mkolodziejczyk92.anonymoussanta.data.entity.Event;
import io.mkolodziejczyk92.anonymoussanta.data.entity.Invitation;
import io.mkolodziejczyk92.anonymoussanta.data.model.InvitationDto;
import io.mkolodziejczyk92.anonymoussanta.data.repository.InvitationRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class InvitationService {

    private final InvitationRepository invitationRepository;

    private final MailSander mailSander = new MailSander();

    public InvitationService(InvitationRepository invitationRepository) {
        this.invitationRepository = invitationRepository;
    }


    @Transactional
    public void setGiftReceiverAndSendEmailToGiver(Long invitationId, Long receiverId) {
        Optional<Invitation> invitationOptional = invitationRepository.findById(invitationId);
        String receiverFullName = invitationRepository.findById(receiverId).get().getFullName();
        invitationOptional.ifPresentOrElse(invitation -> {
            invitation.setGiftReceiver(receiverFullName);

            invitationRepository.save(invitation);
        }, () -> {
            throw new EntityNotFoundException("Invitation dose not exist.");
        });

        invitationOptional.ifPresentOrElse(invitation -> {
            mailSander.sendEmailAfterDraw(
                    invitation.getFullName(),
                    invitation.getEvent().getName(),
                    invitation.getParticipantEmail(),
                    receiverFullName);
        }, () -> {
            throw new EntityNotFoundException("Invitation dose not exist.");
        });

    }

    public List<Invitation> createListOfInvitationEntitiesForSavingEvent(List<InvitationDto> listOfInvitationForEvent, Event event) {
        List<Invitation> invitationEntities = new ArrayList<>();
        for (InvitationDto invitation : listOfInvitationForEvent) {
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

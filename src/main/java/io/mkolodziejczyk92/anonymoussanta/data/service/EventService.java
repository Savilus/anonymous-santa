package io.mkolodziejczyk92.anonymoussanta.data.service;

import io.mkolodziejczyk92.anonymoussanta.data.entity.Event;
import io.mkolodziejczyk92.anonymoussanta.data.entity.Invitation;
import io.mkolodziejczyk92.anonymoussanta.data.model.EventDto;
import io.mkolodziejczyk92.anonymoussanta.data.model.InvitationDto;
import io.mkolodziejczyk92.anonymoussanta.data.repository.EventRepository;
import io.mkolodziejczyk92.anonymoussanta.data.repository.InvitationRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class EventService {
    private final EventRepository eventRepository;
    private final InvitationRepository invitationRepository;
    private final InvitationService invitationService;
    private final UserService userService;
    private final MailSander mailSander = new MailSander();

    public EventService(EventRepository eventRepository,
                        InvitationRepository invitationRepository, InvitationService invitationService,
                        UserService userService) {
        this.eventRepository = eventRepository;
        this.invitationRepository = invitationRepository;
        this.invitationService = invitationService;
        this.userService = userService;
    }

    @Transactional
    public void saveEventAndSendInvitationsToParticipants(EventDto eventDto) {
        List<String> passwordsForInvitations = createPasswordsForInvitations(eventDto);

        Event event = Event.builder()
                .name(eventDto.getName())
                .eventDate(eventDto.getEventDate())
                .numberOfPeople(eventDto.getNumberOfPeople())
                .budget(eventDto.getBudget())
                .currency(eventDto.getCurrency())
                .imageUrl(pickRandomImage())
                .organizer(userService.getUserById(eventDto.getOrganizerId()))
                .build();

        Event eventWithId = eventRepository.save(event);
        List<Invitation> listOfInvitationEntitiesForSavingEvent =
                invitationService.createListOfInvitationEntitiesForSavingEvent(eventDto.getListOfInvitationForEvent(), eventWithId);

        for (Invitation value : listOfInvitationEntitiesForSavingEvent) {
            Invitation invitation = invitationService.addNewInvitation(value);
            mailSander.sendEmailWithInvitation(
                    invitation.getFullName(),
                    eventWithId.getName(),
                    String.valueOf(invitation.getEvent().getId()),
                    invitation.getParticipantEmail(),
                    invitation.getInvitationPassword());
        }
    }


    public void deleteEvent(Long eventId, Long userId) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new EntityNotFoundException("Event does not exist."));

        if (!event.getOrganizer().getId().equals(userId)) {
            throw new RuntimeException("Only the organizer can delete an event.");
        }

        List<Invitation> invitations = event.getListOfInvitationForEvent();
        invitationRepository.deleteAll(invitations);

        eventRepository.deleteById(eventId);
    }

    public List<EventDto> getAllEventsByUserId(Long id) {
        List<Invitation> allAcceptedUserInvitations = invitationService.getAllUserAcceptedInvitations(id);
        List<EventDto> allUserEvents = new ArrayList<>();
        for (Invitation invitation : allAcceptedUserInvitations) {
            Event event = invitation.getEvent();
            allUserEvents.add(EventDto.builder()
                    .id(event.getId())
                    .name(event.getName())
                    .eventDate(event.getEventDate())
                    .numberOfPeople(event.getNumberOfPeople())
                    .budget(event.getBudget())
                    .currency(event.getCurrency())
                    .imageUrl(event.getImageUrl())
                    .giftReceiverForLogInUser(
                            invitation.getGiftReceiver() == null ?
                                    "The draw has not taken place" : "Let's buy gift for: " + invitation.getGiftReceiver())
                    .build());
        }
        List<Event> eventListWhereLogInUserIsOrganizer = eventRepository.findByOrganizerId(id);
        for (Event event : eventListWhereLogInUserIsOrganizer) {

            allUserEvents.add(EventDto.builder()
                    .id(event.getId())
                    .name(event.getName())
                    .eventDate(event.getEventDate())
                    .numberOfPeople(event.getNumberOfPeople())
                    .budget(event.getBudget())
                    .currency(event.getCurrency())
                    .imageUrl(event.getImageUrl())
                    .organizerId(String.valueOf(id))
                    .logInUserIsAnOrganizer(id.equals(event.getOrganizer().getId()))
                    .build());
        }
        return allUserEvents;
    }


    public List<InvitationDto> getAllParticipantsForEventByEventId(Long userId, Long eventId) {
        List<InvitationDto> invitationDtoList = new ArrayList<>();
        eventRepository.findById(eventId).ifPresentOrElse(event -> {
            if (event.getOrganizer().getId().equals(userId)) {
                List<Invitation> allInvitationsForEvent = invitationService.getAllInvitationsForEvent(eventId);
                allInvitationsForEvent.forEach(invitation -> invitationDtoList.add(InvitationDto.builder()
                        .participantName(invitation.getParticipantName())
                        .participantSurname(invitation.getParticipantSurname())
                        .participantEmail(invitation.getParticipantEmail())
                        .participantStatus(invitation.isParticipantStatus()).build()));
            } else {
                throw new RuntimeException("Only organizer can see participants.");
            }
        }, () -> {
            throw new EntityNotFoundException("Event dose not exist.");
        });
        return invitationDtoList;
    }

    public void joinToTheEvent(Map<String, String> request) {
        String eventId = request.get("eventID");
        String invitationPassword = request.get("eventPassword");
        String userEmail = request.get("userEmail");
        Optional<Event> eventOptional = eventRepository.findById(Long.valueOf(eventId));
        eventOptional.ifPresentOrElse(event -> {
            boolean isInputDataCorrect = event.getListOfInvitationForEvent()
                    .stream()
                    .anyMatch(invitation -> invitation.getInvitationPassword().equals(invitationPassword)
                            && invitation.getParticipantEmail().equals(userEmail));

            if (isInputDataCorrect) {
                event.getListOfInvitationForEvent()
                        .stream()
                        .filter(invitation -> invitation.getParticipantEmail().equals(userEmail))
                        .findFirst()
                        .ifPresent(invitation -> {
                            invitation.setUser(userService.getUserByEmail(userEmail));
                            invitation.setParticipantStatus(true);
                        });
                eventRepository.save(event);
            }
        }, () -> {
            throw new EntityNotFoundException("Event does not exist.");
        });
    }

    @Transactional
    public void makeDrawAndSendInformationToParticipantsAndSavePairsInDb(Long eventId, Long userId) {
        eventRepository.findById(eventId).ifPresentOrElse(event -> {
                    if (event.getOrganizer().getId().equals(userId)) {
                        Map<Long, Long> pairsOfDraw = makeADraw(eventId);
                        for (Map.Entry<Long, Long> entry : pairsOfDraw.entrySet()) {
                            Long giver = entry.getKey();
                            Long receiver = entry.getValue();
                            invitationService.setGiftReceiverAndSendEmailToGiver(giver, receiver);
                        }
                    } else {
                        throw new RuntimeException("Only organizer can make a draw.");
                    }
                },
                () -> {
                    throw new EntityNotFoundException("Event dose not exist.");
                });
    }

    private String pickRandomImage() {
        Random random = new Random();
        int imageNumber = random.nextInt(8) + 1;
        return String.valueOf(imageNumber);
    }


    private Map<Long, Long> makeADraw(Long eventId) {
        Map<Long, Long> pairsAfterDraw = new HashMap<>();
        List<Long> invitationsId = new ArrayList<>();

        Optional<Event> eventOptional = eventRepository.findById(eventId);
        eventOptional.ifPresentOrElse(event -> {
            event.getListOfInvitationForEvent().stream()
                    .map(Invitation::getId)
                    .forEach(invitationsId::add);
        }, () -> {
            throw new EntityNotFoundException("Event dose not exist.");
        });

        Collections.shuffle(invitationsId);
        int totalParticipants = invitationsId.size();

        for (int i = 0; i < totalParticipants - 1; i++) {
            Long giverId = invitationsId.get(i);
            Long receiverId = invitationsId.get(i + 1);
            pairsAfterDraw.put(giverId, receiverId);
        }
        Long firstParticipantId = invitationsId.get(0);
        Long lastParticipantId = invitationsId.get(totalParticipants - 1);
        pairsAfterDraw.put(lastParticipantId, firstParticipantId);

        return pairsAfterDraw;
    }

    private List<String> createPasswordsForInvitations(EventDto eventDto) {
        int numberOfInvitations = eventDto.getListOfInvitationForEvent().size();
        List<String> passwordsForInvitations = new ArrayList<>();
        for (int numberOfInvitationsSent = 0; numberOfInvitations > numberOfInvitationsSent; numberOfInvitationsSent++) {
            passwordsForInvitations.add(getEventPassword());
        }
        return passwordsForInvitations;
    }

    public static String getEventPassword() {
        final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        final int PASSWORD_LENGTH = 10;
        StringBuilder password = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < PASSWORD_LENGTH; i++) {
            int index = random.nextInt(CHARACTERS.length());
            char character = CHARACTERS.charAt(index);
            password.append(character);
        }
        return password.toString();
    }


}
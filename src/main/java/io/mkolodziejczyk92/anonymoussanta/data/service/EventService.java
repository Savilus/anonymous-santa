package io.mkolodziejczyk92.anonymoussanta.data.service;

import io.mkolodziejczyk92.anonymoussanta.data.entity.Event;
import io.mkolodziejczyk92.anonymoussanta.data.entity.Invitation;
import io.mkolodziejczyk92.anonymoussanta.data.entity.User;
import io.mkolodziejczyk92.anonymoussanta.data.mapper.EventMapper;
import io.mkolodziejczyk92.anonymoussanta.data.mapper.InvitationMapper;
import io.mkolodziejczyk92.anonymoussanta.data.model.EventDto;
import io.mkolodziejczyk92.anonymoussanta.data.model.InvitationDto;
import io.mkolodziejczyk92.anonymoussanta.data.repository.EventRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class EventService {
    private final EventRepository eventRepository;
    private final InvitationService invitationService;
    private final UserService userService;
    private final EventMapper eventMapper;
    private final InvitationMapper invitationMapper;

    public EventService(EventRepository eventRepository,
                        InvitationService invitationService,
                        UserService userService,
                        EventMapper eventMapper,
                        InvitationMapper invitationMapper) {
        this.eventRepository = eventRepository;
        this.invitationService = invitationService;
        this.userService = userService;
        this.eventMapper = eventMapper;
        this.invitationMapper = invitationMapper;
    }

    @Transactional
    public void sendInvitationsToParticipantsAndSaveEvent(EventDto eventDto) {

        //TODO: send invitations to participants

        Event event = Event.builder()
                .name(eventDto.getName())
                .eventDate(eventDto.getEventDate())
                .numberOfPeople(eventDto.getNumberOfPeople())
                .budget(eventDto.getBudget())
                .currency(eventDto.getCurrency())
                .eventPassword(getEventPassword())
                .organizer(userService.getUserById(eventDto.getOrganizerId()))
                .build();

        Event eventWithId = eventRepository.save(event);
        eventWithId.setListOfInvitationForEvent(
                invitationService.createListOfInvitationEntitiesForSavingEvent(
                        eventDto.getListOfInvitationForEvent(), eventWithId)
        );
        eventRepository.save(eventWithId);
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

    public void deleteEvent(Long id) {
        eventRepository.deleteById(id);
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
                    .giftReceiverForLogInUser(
                            invitation.getGiftReceiver() == null ? "The draw has not taken place" : invitation.getGiftReceiver())
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
                    .listOfInvitationForEvent(invitationMapper.mapToInvitationDtoList(event.getListOfInvitationForEvent()))
                    .build());
        }
        return allUserEvents;
    }

    public List<InvitationDto> getAllParticipantsForEventByEventId(Long eventId) {
        return invitationMapper.mapToInvitationDtoList(invitationService.getAllInvitationsForEvent(eventId));
    }

    public void joinToTheEvent(Map<String, String> request) {
        String eventId = request.get("eventId");
        String eventPassword = request.get("eventPassword");
        String userEmail = request.get("userEmail");

        Optional<Event> eventOptional = eventRepository.findById(Long.valueOf(eventId));
        eventOptional.ifPresentOrElse(event -> {
            if (event.getEventPassword().equals(eventPassword)) {
                event.getListOfInvitationForEvent().stream()
                        .filter(invitation -> invitation.getUser().getEmail().equals(userEmail))
                        .findFirst().get().setParticipantStatus(true);
            }
            eventRepository.save(event);
        }, () -> {
            throw new EntityNotFoundException("Event dose not exist.");
        });
    }

    //TODO: send information to participants
    public void makeDrawAndSendInformationToParticipantsAndSavePairsInDb(Long eventId) {
        Map<Long, Long> pairsOfDraw = makeADraw(eventId);
        for (Map.Entry<Long, Long> entry : pairsOfDraw.entrySet()) {
            Long giver = entry.getKey();
            Long receiver = entry.getValue();
            invitationService.setGiftReceiver(giver, receiver);
        }

    }

    public Map<Long, Long> makeADraw(Long eventId) {
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


}


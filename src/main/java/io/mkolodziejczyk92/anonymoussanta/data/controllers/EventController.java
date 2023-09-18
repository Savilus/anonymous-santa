package io.mkolodziejczyk92.anonymoussanta.data.controllers;

import io.mkolodziejczyk92.anonymoussanta.data.exceptions.EventNotFoundException;
import io.mkolodziejczyk92.anonymoussanta.data.exceptions.OrganizerException;
import io.mkolodziejczyk92.anonymoussanta.data.exceptions.UserNotFoundException;
import io.mkolodziejczyk92.anonymoussanta.data.model.EventDto;
import io.mkolodziejczyk92.anonymoussanta.data.model.InvitationDto;
import io.mkolodziejczyk92.anonymoussanta.data.service.EventService;
import io.mkolodziejczyk92.anonymoussanta.data.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/event")
public class EventController {
    private final EventService eventService;
    private final UserService userService;

    public EventController(EventService eventService,
                           UserService userService) {
        this.eventService = eventService;
        this.userService = userService;
    }

    @PostMapping("/add")
    public ResponseEntity<String> createEvent(@RequestHeader("Authorization") String bearerToken, @RequestBody EventDto eventDto) throws UserNotFoundException {
        Long userId = userService.getUserIdFromToken(bearerToken);
        eventDto.setOrganizerId(String.valueOf(userId));
        eventService.saveEventAndSendInvitationsToParticipants(eventDto);
        return ResponseEntity.ok("Event has been saved.");

    }

    @GetMapping("/user-events")
    public ResponseEntity<List<EventDto>> getAllEventsForLogInUser(@RequestHeader("Authorization") String bearerToken) throws UserNotFoundException {
        Long userId = userService.getUserIdFromToken(bearerToken);
        return ResponseEntity.status(HttpStatus.OK).body(eventService.getAllEventsByUserId(userId));
    }

    @DeleteMapping("/delete/{eventId}")
    public ResponseEntity<String> deleteEventById(@PathVariable(name = "eventId") Long eventId,
                                                  @RequestHeader("Authorization") String bearerToken) throws UserNotFoundException, OrganizerException, EventNotFoundException {
        Long userId = userService.getUserIdFromToken(bearerToken);
        eventService.deleteEvent(eventId, userId);
        return ResponseEntity.ok("Event with id: " + eventId + " has been deleted");

    }

    @GetMapping("/participants-by-event-id/{eventId}")
    public ResponseEntity<List<InvitationDto>> getAllParticipantsForEvent(@RequestHeader("Authorization") String bearerToken,
                                                                          @PathVariable(name = "eventId") Long eventId) throws UserNotFoundException, OrganizerException, EventNotFoundException {
        Long userId = userService.getUserIdFromToken(bearerToken);
        return ResponseEntity.status(HttpStatus.OK).body(eventService.getAllParticipantsForEventByEventId(userId, eventId));

    }

    @PostMapping("/join-to-the-event")
    public ResponseEntity<String> joinToTheEvent(@RequestBody Map<String, String> request) throws EventNotFoundException {
        eventService.joinToTheEvent(request);
        return ResponseEntity.ok("You joined to the event.");
    }

    @PostMapping("/draw/{eventId}")
    public ResponseEntity<String> performAPairDraw(@PathVariable Long eventId,
                                                   @RequestHeader("Authorization") String bearerToken) throws UserNotFoundException, OrganizerException, EventNotFoundException {
        Long userId = userService.getUserIdFromToken(bearerToken);
        eventService.makeDrawAndSendInformationToParticipantsAndSavePairsInDb(eventId, userId);
        return ResponseEntity.ok("The draw has been made");

    }

}
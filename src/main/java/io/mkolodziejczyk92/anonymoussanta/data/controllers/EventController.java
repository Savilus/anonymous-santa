package io.mkolodziejczyk92.anonymoussanta.data.controllers;

import io.mkolodziejczyk92.anonymoussanta.data.config.JwtService;
import io.mkolodziejczyk92.anonymoussanta.data.model.EventDto;
import io.mkolodziejczyk92.anonymoussanta.data.model.InvitationDto;
import io.mkolodziejczyk92.anonymoussanta.data.service.EventService;
import io.mkolodziejczyk92.anonymoussanta.data.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/event")
public class EventController {
    private final EventService eventService;
    private final JwtService jwtService;
    private final UserService userService;

    public EventController(EventService eventService,
                           JwtService jwtService,
                           UserService userService) {
        this.eventService = eventService;
        this.jwtService = jwtService;
        this.userService = userService;
    }

    @PostMapping("/add")
    public ResponseEntity<String> createEvent(@RequestHeader("Authorization") String bearerToken, @RequestBody EventDto eventDto) {
        try {
            String token = bearerToken.substring(7);
            String extractedUsername = jwtService.extractUserName(token);
            Long userId = userService.getUserIdByUsernameAsMail(extractedUsername);
            eventDto.setOrganizerId(String.valueOf(userId));
            eventService.saveEventAndSendInvitationsToParticipants(eventDto);
            return ResponseEntity.ok("Event has been saved.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while trying to save the event.");
        }
    }

    @GetMapping("/user-events")
    public ResponseEntity<List<EventDto>> getAllEventsForLogInUser(@RequestHeader("Authorization") String bearerToken) {
        try {
            String token = bearerToken.substring(7);
            String extractedUsername = jwtService.extractUserName(token);
            Long userId = userService.getUserIdByUsernameAsMail(extractedUsername);
            return ResponseEntity.status(HttpStatus.OK).body(eventService.getAllEventsByUserId(userId));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ArrayList<>());
        }
    }

    @DeleteMapping("/delete/{eventId}")
    public ResponseEntity<String> deleteEventById(@PathVariable(name = "eventId") Long eventId,
                                                  @RequestHeader("Authorization") String bearerToken) {
        try {
            String token = bearerToken.substring(7);
            String extractedUsername = jwtService.extractUserName(token);
            Long userId = userService.getUserIdByUsernameAsMail(extractedUsername);
            eventService.deleteEvent(eventId, userId);
            return ResponseEntity.ok("Event has been deleted");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while trying to delete the event.");
        }
    }

    @GetMapping("/participants-by-event-id/{eventId}")
    public ResponseEntity<List<InvitationDto>> getAllParticipantsForEvent(@RequestHeader("Authorization") String bearerToken,
                                                                          @PathVariable(name = "eventId") Long eventId) {
        try {
            String token = bearerToken.substring(7);
            String extractedUsername = jwtService.extractUserName(token);
            Long userId = userService.getUserIdByUsernameAsMail(extractedUsername);
            return ResponseEntity.status(HttpStatus.OK).body(eventService.getAllParticipantsForEventByEventId(userId, eventId));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ArrayList<>());
        }
    }

    @PostMapping("/join-to-the-event")
    public ResponseEntity<String> joinToTheEvent(@RequestBody Map<String, String> request) {
        try {
            eventService.joinToTheEvent(request);
            return ResponseEntity.ok("You joined to the event.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while trying to join the event.");
        }
    }

    @PostMapping("/draw/{eventId}/{userId}")
    public ResponseEntity<String> performAPairDraw(@PathVariable Long eventId, @PathVariable Long userId) {
        try {
            eventService.makeDrawAndSendInformationToParticipantsAndSavePairsInDb(eventId, userId);
            return ResponseEntity.ok("The draw has been made");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred during the execution of the draw.");
        }
    }

}
package io.mkolodziejczyk92.anonymoussanta.data.repository;

import io.mkolodziejczyk92.anonymoussanta.data.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    void deleteById(Long eventId);
    List<Event> findByOrganizerId(Long organizerId);
    Optional<Event> findByEventCode(String eventCode);

}

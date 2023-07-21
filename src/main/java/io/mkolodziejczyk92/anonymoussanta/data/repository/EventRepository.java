package io.mkolodziejczyk92.anonymoussanta.data.repository;

import io.mkolodziejczyk92.anonymoussanta.data.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    void deleteById(Long eventId);
    List<Event> findByOrganizerId(Long organizerId);

}

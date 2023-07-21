package io.mkolodziejczyk92.anonymoussanta.data.repository;

import io.mkolodziejczyk92.anonymoussanta.data.entity.Invitation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvitationRepository extends JpaRepository<Invitation, Long> {

    List<Invitation> findAllByParticipantStatusIsTrueAndUserId(Long userId);

    List<Invitation> findAllByEventId(Long eventId);
}

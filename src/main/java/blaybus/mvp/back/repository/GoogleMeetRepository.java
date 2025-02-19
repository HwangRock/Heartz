package blaybus.mvp.back.repository;

import blaybus.mvp.back.domain.GoogleMeetEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface GoogleMeetRepository extends JpaRepository<GoogleMeetEntity, String> {
    @Query("SELECT g.eventId FROM GoogleMeetEntity g WHERE g.reservation.id = :reservationId")
    String findEventIdByReservationId(@Param("reservationId") Long reservationId);
    @Transactional
    void deleteByReservationId(Long reservationId);
}
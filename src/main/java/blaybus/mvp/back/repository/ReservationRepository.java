package blaybus.mvp.back.repository;

import blaybus.mvp.back.domain.Reservation;
import blaybus.mvp.back.domain.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findByUserId(Long userId);
    List<Reservation> findByUserIdAndStatus(Long userId, ReservationStatus status);

}

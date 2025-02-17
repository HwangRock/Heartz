package blaybus.mvp.back.repository;

import blaybus.mvp.back.domain.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findByUserId(String userId);
    List<Reservation> findByEmailAndStatus(String email, String status);

}

package blaybus.mvp.back.repository;

import blaybus.mvp.back.domain.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
}

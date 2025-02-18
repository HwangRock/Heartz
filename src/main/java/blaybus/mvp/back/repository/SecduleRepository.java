package blaybus.mvp.back.repository;

import blaybus.mvp.back.domain.Secdule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

public interface SecduleRepository extends JpaRepository<Secdule, Long> {

    List<Secdule> findByDesignerIdAndDate(Long designerId, LocalDate date);
    @Transactional
    void deleteByReservationId(Long reservationId);
}

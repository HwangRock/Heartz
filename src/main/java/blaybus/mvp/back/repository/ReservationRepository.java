package blaybus.mvp.back.repository;

import blaybus.mvp.back.domain.Designer;
import blaybus.mvp.back.domain.Reservation;
import blaybus.mvp.back.domain.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    //@Query("SELECT * FROM Reservation r WHERE r.userId = userId ORDER BY r.createdAt DESC")
    List<Reservation> findByUserIdOrderByCreatedAtDesc(Long userId);
    List<Reservation> findByUserIdAndStatusAndDateAndDesigner(Long userId, ReservationStatus status, LocalDate date, Designer designer);
    @Transactional
    void deleteById(Long reservationId);
    @Modifying
    @Transactional
    @Query("UPDATE Reservation r SET r.status = 'CANCELED' WHERE r.id = :reservationId")
    void changeStatus(Long reservationId);

    @Modifying
    @Query("UPDATE Reservation r SET r.status = :status WHERE r.id = :reservationId")
    void updateStatus(@Param("reservationId")Long reservationId, @Param("status")ReservationStatus status);

    @Modifying
    @Query("UPDATE Reservation r SET r.comment = :comment WHERE r.id = :reservationId")
    void saveComment(@Param("reservationId")Long reservationId, @Param("comment")String comment);

}

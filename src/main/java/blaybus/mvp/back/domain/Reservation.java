package blaybus.mvp.back.domain;

import blaybus.mvp.back.dto.request.ReservationRequestDTO;
import blaybus.mvp.back.dto.request.ReservationSaveRequestDTO;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@RequiredArgsConstructor
@Getter
@DynamicUpdate
@Table(name = "reservation", schema = "test")
public class Reservation {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false, referencedColumnName = "id")
    private Client user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "designer_id", nullable = false, referencedColumnName = "designer_id")
    private Designer designer;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "time", nullable = false)
    private LocalTime time;

    @Column(name = "is_online", nullable = false)
    private Boolean isOnline = false;

    @Column(name = "meet_link")
    private String meetLink;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ReservationStatus status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "comment")
    private String comment;

    @Builder
    public Reservation(Client user, Designer designer, LocalDate date, LocalTime time, Boolean isOnline, String meetLink, ReservationStatus status, LocalDateTime createdAt, String comment) {
        this.user = user;
        this.designer = designer;
        this.date = date;
        this.time = time;
        this.isOnline = isOnline;
        this.meetLink = meetLink;
        this.status = status;
        this.createdAt = createdAt;
        this.comment = comment;
    }

    public Reservation(ReservationSaveRequestDTO reservationSaveRequestDTO) {
        this(reservationSaveRequestDTO.getClient(),
                reservationSaveRequestDTO.getDesigner(),
                reservationSaveRequestDTO.getDate(),
                reservationSaveRequestDTO.getTime(),
                reservationSaveRequestDTO.isOnline(),
                reservationSaveRequestDTO.getMeetLink(),
                reservationSaveRequestDTO.getStatus(),
                reservationSaveRequestDTO.getCreatedAt(),
                reservationSaveRequestDTO.getComment()
                );
    }
}
package blaybus.mvp.back.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;


import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@RequiredArgsConstructor
@Getter
@DynamicUpdate
@Table(name = "secdule", schema = "test")
public class Secdule {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "designer_id", nullable = false, referencedColumnName = "designer_id")
    private Designer designer;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "reservation_id", nullable = false, referencedColumnName = "id")
    private Reservation reservation;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "start_time")
    private LocalTime startTime;

    @Column(name = "end_time")
    private LocalTime endTime;

    @Builder
    public Secdule(Designer designer, Reservation reservation, LocalDate date, LocalTime startTime, LocalTime endTime) {
        this.designer = designer;
        this.reservation = reservation;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
    }

}
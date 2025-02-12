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

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "start_time")
    private LocalTime startTime;

    @Column(name = "end_time")
    private LocalTime endTime;

    @Column(name = "status")
    private Boolean status;

    @Builder
    public Secdule(Designer designer, LocalDate date, LocalTime startTime, LocalTime endTime, Boolean status) {
        this.designer = designer;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = status;
    }

}
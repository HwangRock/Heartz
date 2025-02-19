package blaybus.mvp.back.domain;

import blaybus.mvp.back.dto.request.PaymentRequestDTO;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@RequiredArgsConstructor
@Getter
@Setter
@DynamicUpdate
@Table(name = "googleMeet", schema = "test")
public class GoogleMeetEntity {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="event_id")
    private String eventId;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "reservation_id", nullable = false, referencedColumnName = "id")
    private Reservation reservation;

    @Builder
    public GoogleMeetEntity(String eventId, Reservation reservation) {
        this.eventId = eventId;
        this.reservation = reservation;
    }

}

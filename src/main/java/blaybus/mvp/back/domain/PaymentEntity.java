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
@Table(name = "payment", schema = "test")
public class PaymentEntity {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="impuid")
    private String impuid;

    @Column(name = "amount")
    private Long amount;

    @Column(name = "payment_status")
    private String paymentStatus;

    @Column(name = "create_at")
    private String createAt;

    @Column(name= "userName")
    private String userName;

    @Column(name="userEmail")
    private String userEmail;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "reservation_id", nullable = false, referencedColumnName = "id")
    private Reservation reservation;

    @Builder
    public PaymentEntity(PaymentRequestDTO dto) {
        this.amount = dto.getAmount();
        this.paymentStatus = dto.getStatus();
        this.createAt = dto.getCreateDate();
        this.userName=dto.getName();
        this.userEmail=dto.getEmail();
        this.impuid=dto.getImpuid();
        this.reservation=dto.getReservation();
    }
}
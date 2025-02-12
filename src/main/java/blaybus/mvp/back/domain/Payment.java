package blaybus.mvp.back.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import java.time.Instant;

@Entity
@RequiredArgsConstructor
@Getter
@DynamicUpdate
@Table(name = "payment", schema = "test")
public class Payment {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "secdule_id", nullable = false)
    private Secdule secdule;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "designer_id", nullable = false, referencedColumnName = "designer_id")
    private Designer designer;

    @Column(name = "amount")
    private Integer amount;

    @Column(name = "payment_method")
    private String paymentMethod;

    @Column(name = "payment_status")
    private Boolean paymentStatus;

    @Column(name = "create_at")
    private Instant createAt;

    @Builder
    public Payment(Secdule secdule, Designer designer, Integer amount, String paymentMethod, Boolean paymentStatus, Instant createAt) {
        this.secdule = secdule;
        this.designer = designer;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.paymentStatus = paymentStatus;
        this.createAt = createAt;
    }
}
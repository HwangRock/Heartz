package blaybus.mvp.back.repository;

import blaybus.mvp.back.domain.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

}

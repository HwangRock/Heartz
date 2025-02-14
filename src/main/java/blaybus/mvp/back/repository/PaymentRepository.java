package blaybus.mvp.back.repository;

import blaybus.mvp.back.domain.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<PaymentEntity, String> {
    public long countByImpuidContainsIgnoreCase(String impuid); // 결제 고유 번호 중복 확인
}

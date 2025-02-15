package blaybus.mvp.back.repository;

import blaybus.mvp.back.domain.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentRepository extends JpaRepository<PaymentEntity, String> {
    public long countByImpuidContainsIgnoreCase(String impuid); // 결제 고유 번호 중복 확인
    public List<PaymentEntity> findAllByUserEmail(String userEmail); // 유저 이메일로 모든 기록 조회
    public PaymentEntity findByImpuid(String impuid);
}

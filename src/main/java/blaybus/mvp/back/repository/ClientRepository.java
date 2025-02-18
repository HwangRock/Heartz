package blaybus.mvp.back.repository;

import blaybus.mvp.back.domain.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ClientRepository extends JpaRepository<Client, Long> {
    Optional<Client> findByUserId(String userId);  // Google의 sub 값을 userId로 저장했으므로 이를 기준으로 조회
}

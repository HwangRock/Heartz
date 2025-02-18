package blaybus.mvp.back.repository;

import blaybus.mvp.back.domain.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ClientRepository extends JpaRepository<Client, Long> {

    Optional<Client> findByEmail(String email); //중복 가입 확인

    @Query("SELECT c.id FROM Client c WHERE c.email = :email")
    Long findUserIdByEmail(String email);

    @Query("SELECT c.name FROM Client c WHERE c.email = :email")
    String findNameByEmail(String email);

    Optional<Client> findByUserId(String userId);  // Google의 sub 값을 userId로 저장했으므로 이를 기준으로 조회
}

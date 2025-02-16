package blaybus.mvp.back.repository;


import blaybus.mvp.back.domain.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClientRepository extends JpaRepository<Client, Long> {

    Optional<Client> findByEmail(String email); //중복 가입 확인


}

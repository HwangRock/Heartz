package blaybus.mvp.back.repository;


import blaybus.mvp.back.domain.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, Long> {

}

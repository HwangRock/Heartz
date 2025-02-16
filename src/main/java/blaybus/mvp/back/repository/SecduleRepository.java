package blaybus.mvp.back.repository;

import blaybus.mvp.back.domain.Secdule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SecduleRepository extends JpaRepository<Secdule, Long> {
}

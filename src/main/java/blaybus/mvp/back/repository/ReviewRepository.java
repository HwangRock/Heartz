package blaybus.mvp.back.repository;

import blaybus.mvp.back.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
}

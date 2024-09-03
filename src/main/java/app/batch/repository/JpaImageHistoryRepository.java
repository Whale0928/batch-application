package app.batch.repository;

import app.batch.domain.ImageHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaImageHistoryRepository extends JpaRepository<ImageHistory, Long> {
}

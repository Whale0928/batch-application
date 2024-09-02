package app.batch.core.entity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ConsoleHistoryRepository extends JpaRepository<ConsoleHistory, UUID> {
}

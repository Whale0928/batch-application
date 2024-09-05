package app.batch.repository;

import app.batch.domain.Mapping;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaMappingRepository extends JpaRepository<Mapping, Long> {
}

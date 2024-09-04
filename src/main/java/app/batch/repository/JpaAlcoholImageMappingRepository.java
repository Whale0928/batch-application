package app.batch.repository;

import app.batch.domain.AlcoholImageMapping;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaAlcoholImageMappingRepository extends JpaRepository<AlcoholImageMapping, Long> {
}

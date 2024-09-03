package app.batch.repository;

import app.batch.domain.Alcohol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JpaAlcoholRepository extends JpaRepository<Alcohol, Long> {

    @Query("SELECT a.imageUrl FROM alcohol a")
    List<String> findAllImages();
}

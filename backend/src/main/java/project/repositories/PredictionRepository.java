package project.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import project.model.Prediction;
import project.model.Role;
import project.model.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface PredictionRepository extends JpaRepository<Prediction,Integer> {

    @Query(value="SELECT * FROM prediction p WHERE p.user_id=:userId", nativeQuery = true)
    public Optional<List<Prediction>> findByUserId(@Param("userId") int userId);

}

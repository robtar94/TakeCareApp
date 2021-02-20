package project.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.model.ParameterDouble;
import project.model.Role;

@Repository
public interface ParameterDoubleRepository extends JpaRepository<ParameterDouble,Integer> {
}

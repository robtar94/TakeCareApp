package project.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.model.ParameterInt;
import project.model.Role;

@Repository
public interface ParameterIntRepository extends JpaRepository<ParameterInt,Integer> {
}

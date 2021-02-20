package project.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import project.model.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role,Integer> {

    public Role findById(int id);

    @Query(value="SELECT * FROM roles r WHERE r.name LIKE %:name%", nativeQuery = true)
    public Role findByName(@Param("name") String name);
}

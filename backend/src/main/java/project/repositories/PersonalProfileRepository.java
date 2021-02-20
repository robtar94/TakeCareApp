package project.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import project.model.PersonalProfile;
import project.model.Role;
import project.model.User;

@Repository
public interface PersonalProfileRepository extends JpaRepository<PersonalProfile,Integer> {

    @Query(value="SELECT * FROM personal_profile pp WHERE pp.user_id=:userId", nativeQuery = true)
    public PersonalProfile findByUserId(@Param("userId") int userId);
}

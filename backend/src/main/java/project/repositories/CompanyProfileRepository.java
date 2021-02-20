package project.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import project.model.CompanyProfile;
import project.model.PersonalData;
import project.model.PersonalProfile;

@Repository
public interface CompanyProfileRepository extends JpaRepository<CompanyProfile,Integer> {

    @Query(value="SELECT * FROM company_profile cp WHERE cp.user_id=:userId", nativeQuery = true)
    public CompanyProfile findByUserId(@Param("userId") int userId);

    @Query(value="SELECT COUNT(*) FROM company_part cp WHERE cp.company_id=:companyId AND cp.personal_id=:personalId", nativeQuery = true)
    public int countCompanyPart(@Param("companyId") int companyId,@Param("personalId") int personalId);
}

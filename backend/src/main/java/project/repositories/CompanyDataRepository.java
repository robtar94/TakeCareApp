package project.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.model.CompanyData;
import project.model.PersonalData;

@Repository
public interface CompanyDataRepository extends JpaRepository<CompanyData,Integer> {

}

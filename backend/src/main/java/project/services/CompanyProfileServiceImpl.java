package project.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.model.CompanyProfile;
import project.model.PersonalProfile;
import project.repositories.CompanyProfileRepository;

import java.util.List;
import java.util.Optional;

@Service
public class CompanyProfileServiceImpl implements CompanyProfileService {

    @Autowired
    private CompanyProfileRepository companyProfileRepository;

    @Override
    public CompanyProfile getCompanyProfile(int id) {
        Optional<CompanyProfile> optionalCompanyProfile = companyProfileRepository.findById(id);
        if(optionalCompanyProfile.isPresent()){
            return optionalCompanyProfile.get();
        }else{
            return null;
        }
    }

    @Override
    public CompanyProfile addCompanyProfile(CompanyProfile companyProfile) {
        return companyProfileRepository.save(companyProfile);
    }

    @Override
    public CompanyProfile getCompanyProfileByUserId(int userId) {
        return companyProfileRepository.findByUserId(userId);
    }

    @Override
    public boolean isCompanyAuthorized(int companyId, int personalId) {
        int c = companyProfileRepository.countCompanyPart(companyId,personalId);
        if (c==0){return false;}
        else {return true;}
    }

    @Override
    public List<CompanyProfile> getAllCompanies() {
        System.out.println(companyProfileRepository.findAll().size());
        return companyProfileRepository.findAll();
    }
    //    @Override
//    public boolean deleteCompanyProfile(int id) {
//        companyProfileRepository.deleteById(id);
//        return true;
//    }
}

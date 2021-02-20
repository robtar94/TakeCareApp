package project.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.model.CompanyData;
import project.model.CompanyProfile;
import project.repositories.CompanyDataRepository;

import java.util.Optional;

@Service
public class CompanyDataServiceImpl implements CompanyDataService {


    @Autowired
    private CompanyDataRepository companyDataRepository;

    @Override
    public CompanyData getCompanyData(int id) {
        Optional<CompanyData> optionalCompanyData = companyDataRepository.findById(id);
        if(optionalCompanyData.isPresent()){
            return optionalCompanyData.get();
        }else{
            return null;
        }
    }

    @Override
    public CompanyData addCompanyData(CompanyData companyData) {
        return companyDataRepository.save(companyData);
    }

//    @Override
//    public boolean deleteCompanyData(int id) {
//        companyDataRepository.deleteById(id);
//    }
}

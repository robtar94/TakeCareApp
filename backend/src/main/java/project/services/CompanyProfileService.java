package project.services;

import project.model.CompanyData;
import project.model.CompanyProfile;
import project.model.PersonalProfile;

import java.util.List;

public interface CompanyProfileService {

    public CompanyProfile getCompanyProfile(int id);
    public CompanyProfile addCompanyProfile(CompanyProfile companyProfile);
//    public boolean deleteCompanyProfile(int id);
    public CompanyProfile getCompanyProfileByUserId(int userId);
    public boolean isCompanyAuthorized(int companyId,int personalId);
    public List<CompanyProfile> getAllCompanies();
}

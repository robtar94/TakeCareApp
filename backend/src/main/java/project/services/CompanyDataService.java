package project.services;

import project.model.CompanyData;

public interface CompanyDataService {

    public CompanyData getCompanyData(int id);
    public CompanyData addCompanyData(CompanyData companyData);
//    public boolean deleteCompanyData(int id);
}

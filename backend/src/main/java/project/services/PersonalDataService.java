package project.services;

import project.model.CompanyProfile;
import project.model.PersonalData;

public interface PersonalDataService {
    public PersonalData getPersonalData(int id);
    public PersonalData addPersonalData(PersonalData personalData);
//    public boolean deletePersonalData(int id);
}

package project.services;

import project.model.PersonalData;
import project.model.PersonalProfile;

public interface PersonalProfileService {

    public PersonalProfile getPersonalProfile(int id);
    public PersonalProfile addPersonalProfile(PersonalProfile personalProfile);
//    public boolean deletePersonalProfile(int id);
    public PersonalProfile getPersonalProfileByUserId(int userId);
}

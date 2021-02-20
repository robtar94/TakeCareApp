package project.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.model.PersonalProfile;
import project.repositories.PersonalProfileRepository;

import java.util.Optional;

@Service
public class PersonalProfileServiceImpl implements PersonalProfileService {

    @Autowired
    private PersonalProfileRepository personalProfileRepository;

    @Override
    public PersonalProfile getPersonalProfile(int id) {
        Optional<PersonalProfile> optionalPersonalProfile = personalProfileRepository.findById(id);
        if(optionalPersonalProfile.isPresent()){
            return optionalPersonalProfile.get();
        }else{
            return null;
        }
    }

    @Override
    public PersonalProfile addPersonalProfile(PersonalProfile personalProfile) {
        return personalProfileRepository.save(personalProfile);
    }

//    @Override
//    public boolean deletePersonalProfile(int id) {
//        personalProfileRepository.deleteById(id);
//    }

    @Override
    public PersonalProfile getPersonalProfileByUserId(int userId) {
        return personalProfileRepository.findByUserId(userId);

    }
}

package project.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.model.PersonalData;
import project.model.PersonalProfile;
import project.repositories.PersonalDataRepository;

import java.util.Optional;

@Service
public class PersonalDataServiceImpl implements PersonalDataService {

    @Autowired
    private PersonalDataRepository personalDataRepository;

    @Override
    public PersonalData getPersonalData(int id) {
        Optional<PersonalData> optionalPersonalData = personalDataRepository.findById(id);
        if(optionalPersonalData.isPresent()){
            return optionalPersonalData.get();
        }else{
            return null;
        }
    }

    @Override
    public PersonalData addPersonalData(PersonalData personalData) {
        return personalDataRepository.save(personalData);
    }

//    @Override
//    public boolean deletePersonalData(int id) {
//
//        personalDataRepository.deleteById(id);
//
//    }
}

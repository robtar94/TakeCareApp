package project.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.DTO.*;
import project.model.CompanyProfile;
import project.model.PersonalProfile;
import project.model.Prediction;
import project.model.User;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Service
public class GeneratorDTO implements Serializable {

    public UserDTO generateUserDTO(User user){
        UserDTO userDTO = new UserDTO(user);
        RoleDTO roleDTO = new RoleDTO(user.getRole().getName());
        userDTO.setRoleDTO(roleDTO);

        return userDTO;

    }
    public List<PredictionDTO> generatePredictionsDTO(
    List<Prediction> predictionList){
        List<PredictionDTO> predictionDTOS = new ArrayList<>();
        for (Prediction prediction:predictionList) {
            PredictionDTO predictionDTO = new PredictionDTO();
            predictionDTO.setId(prediction.getId());
            predictionDTO.setName(prediction.getName());
            predictionDTO.setLocalDateTime(prediction.getLocalDateTime());
            predictionDTO.setResultValue(prediction.getResultValue());
            predictionDTOS.add(predictionDTO);
        }

        return predictionDTOS;
    }

    public PersonalProfileDTO generatePersonalProfileDTO(PersonalProfile personalProfile){
        PersonalDataDTO personalDataDTO = new PersonalDataDTO(personalProfile.getPersonalData());
        UserDTO userDTO = generateUserDTO(personalProfile.getUser());

        PersonalProfileDTO personalProfileDTO = new PersonalProfileDTO(personalDataDTO,userDTO);
        return personalProfileDTO;

    }
    public CompanyProfileDTO generateCompanyProfileDTO(CompanyProfile companyProfile){
        CompanyDataDTO companyDataDTO = new CompanyDataDTO(companyProfile.getCompanyData());
        UserDTO userDTO = generateUserDTO(companyProfile.getUser());

        return new CompanyProfileDTO(companyDataDTO,userDTO);
    }
}

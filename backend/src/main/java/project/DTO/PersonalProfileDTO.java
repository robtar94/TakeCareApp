package project.DTO;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Table;
import project.model.User;

import javax.persistence.*;
import java.io.Serializable;


public class PersonalProfileDTO implements Serializable {

    @Getter
    @Setter
    private int id;

    @Getter
    @Setter
    private PersonalDataDTO personalDataDTO;

    @Getter
    @Setter
    private UserDTO userDTO;

    public PersonalProfileDTO(){};

    public PersonalProfileDTO(PersonalDataDTO personalDataDTO, UserDTO userDTO) {
        this.personalDataDTO = personalDataDTO;
        this.userDTO = userDTO;
    }

    public PersonalProfileDTO(PersonalDataDTO personalDataDTO) {
        this.personalDataDTO = personalDataDTO;
    }
}

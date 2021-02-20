package project.DTO;

import lombok.Getter;
import lombok.Setter;
import project.model.PersonalData;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;


public class PersonalDataDTO implements Serializable {

    @Getter
    @Setter
    private int id;

    @Getter
    @Setter
    private String name;

    @Getter
    @Setter
    private String surname;

    @Getter
    @Setter
    private String email;

    @Getter
    @Setter
    private String gender;

    @Getter
    @Setter
    private Date datebirth;

    public PersonalDataDTO(){};

    public PersonalDataDTO(String name, String surname, String email, String gender, Date datebirth) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.gender = gender;
        this.datebirth = datebirth;
    }

    public PersonalDataDTO(PersonalData personalData){
        this.name = personalData.getName();
        this.surname = personalData.getSurname();
        this.email = personalData.getEmail();
        this.gender = personalData.getGender();
        this.datebirth = personalData.getDatebirth();
    };
}

package project.DTO;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Table;
import project.model.User;

import javax.persistence.*;
import java.io.Serializable;


public class CompanyProfileDTO implements Serializable {
    @Getter
    @Setter
    private int id;

    @Getter
    @Setter
    private CompanyDataDTO companyData;


    @Getter
    @Setter
    private UserDTO userDTO;

    public CompanyProfileDTO(){};

    public CompanyProfileDTO(CompanyDataDTO companyData, UserDTO userDTO) {
        this.companyData = companyData;
        this.userDTO = userDTO;
    }

    public CompanyProfileDTO(CompanyDataDTO companyData) {
        this.companyData = companyData;
    }
}

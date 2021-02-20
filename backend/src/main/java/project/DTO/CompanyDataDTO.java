package project.DTO;

import lombok.Getter;
import lombok.Setter;
import project.model.CompanyData;

import javax.persistence.*;
import java.io.Serializable;


public class CompanyDataDTO implements Serializable {

    @Getter
    @Setter
    private int id;

    @Getter
    @Setter
    private String name;

    @Getter
    @Setter
    private String email;

    @Getter
    @Setter
    private String address;

    @Getter
    @Setter
    private double longitude;

    @Getter
    @Setter
    private double latitude;


    public CompanyDataDTO(){};

    public CompanyDataDTO(String name, String email, String address, double longitude, double latitude) {
        this.name = name;
        this.email = email;
        this.address = address;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public CompanyDataDTO(CompanyData companyData){
        this.id = companyData.getId();
        this.name = companyData.getName();
        this.email = companyData.getEmail();
        this.address = companyData.getAddress();
        this.longitude = companyData.getLongitude();
        this.latitude = companyData.getLatitude();
    };
}

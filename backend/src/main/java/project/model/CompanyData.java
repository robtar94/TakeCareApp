package project.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
@Entity
@Table(name="CompanyData")
public class CompanyData implements Serializable {

    @Getter
    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id",nullable = false)
    private int id;

    @Getter
    @Setter
    @Column(name="name",nullable = false)
    private String name;

    @Getter
    @Setter
    @Column(name="email",nullable = false)
    private String email;

    @Getter
    @Setter
    @Column(name="address",nullable = false)
    private String address;

    @Getter
    @Setter
    @Column(name="longitude",nullable = false)
    private double longitude;

    @Getter
    @Setter
    @Column(name="latitude",nullable = false)
    private double latitude;

    @Getter
    @Setter
    @OneToOne(mappedBy = "companyData")
    private CompanyProfile companyProfile;

    public CompanyData(){};

}

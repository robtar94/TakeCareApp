package project.model;

import lombok.Getter;
import lombok.Setter;


import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name="PersonalProfile")
public class PersonalProfile implements Serializable {

    @Getter
    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id",nullable=false)
    private int id;

    @Getter
    @Setter
    @OneToOne(cascade=CascadeType.PERSIST)
    @JoinColumn(name="personalDateId",referencedColumnName = "id")
    private PersonalData personalData;

    @Getter
    @Setter
    @OneToOne(cascade=CascadeType.PERSIST)
    @JoinColumn(name="userId",referencedColumnName = "id")
    private User user;

    @Getter
    @Setter
    @ManyToMany(mappedBy = "personalProfiles")
    List<CompanyProfile> companyProfileList;




    public PersonalProfile(){};

    public PersonalProfile(PersonalData personalData, User user) {
        this.personalData = personalData;
        this.user = user;
    }

    public PersonalProfile(PersonalData personalData) {
        this.personalData = personalData;
    }

    public PersonalProfile(PersonalData personalData, User user, List<CompanyProfile> companyProfileList) {
        this.personalData = personalData;
        this.user = user;
        this.companyProfileList = companyProfileList;
    }
}

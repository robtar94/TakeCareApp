package project.model;

import lombok.Getter;
import lombok.Setter;


import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name="CompanyProfile")
public class CompanyProfile implements Serializable {
    @Getter
    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id",nullable = false)
    private int id;

    @Getter
    @Setter
    @OneToOne(cascade=CascadeType.PERSIST)
    @JoinColumn(name="companyDataId",referencedColumnName = "id")
    private CompanyData companyData;

    @Getter
    @Setter
    @ManyToMany
    @JoinTable(
            name="company_part",
            joinColumns = @JoinColumn(name = "companyId"),
            inverseJoinColumns = @JoinColumn(name="personalId")
    )
    private List<PersonalProfile> personalProfiles;


    @Getter
    @Setter
    @OneToOne(cascade=CascadeType.PERSIST)
    @JoinColumn(name="userId",referencedColumnName = "id")
    private User user;

    public CompanyProfile(){};

    public CompanyProfile(CompanyData companyData, List<PersonalProfile> personalProfiles, User user) {
        this.companyData = companyData;
        this.personalProfiles = personalProfiles;
        this.user = user;
    }

    public CompanyProfile(CompanyData companyData, User user) {
        this.companyData = companyData;
        this.user = user;
    }

    public CompanyProfile(CompanyData companyData) {
        this.companyData = companyData;
    }
}

package project.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name="PersonalData")
public class PersonalData implements Serializable {

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
    @Column(name="surname",nullable = false)
    private String surname;

    @Getter
    @Setter
    @Column(name="email",nullable = false)
    private String email;

    @Getter
    @Setter
    @Column(name="gender",nullable = false)
    private String gender;

    @Getter
    @Setter
    @Column(name="datebirth",nullable = false)
    @Basic
    @Temporal(TemporalType.DATE)
    private Date datebirth;

    @Getter
    @Setter
    @OneToOne(mappedBy = "personalData")
    private PersonalProfile personalProfile;

    public PersonalData(){};
}

package project.model;


import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Entity
@Table(name="Users")
public class User implements Serializable {

    @Getter
    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id",nullable = false)
    private int id;

    @Getter
    @Setter
    @Column(name="login",nullable = false,unique = true)
    private String login;

    @Getter
    @Setter
    @Column(name="password",nullable = false)
    private String password;

    @Getter
    @Setter
    @Column(name="enabled",columnDefinition = "boolean default true")
    private boolean enabled;

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "role",nullable=false)
    private Role role;

    @Getter
    @Setter
    @OneToMany(mappedBy = "user",orphanRemoval = true,cascade = CascadeType.PERSIST)
    @JsonIgnore
    private List<Prediction> predictions;

    @Getter
    @Setter
    @OneToMany(mappedBy = "creator",orphanRemoval = true,cascade = CascadeType.PERSIST)
    @JsonIgnore
    private List<Prediction> predictionsCreated;

    @Getter
    @Setter
    @OneToOne(mappedBy = "user")
    private CompanyProfile companyProfile;

    @Getter
    @Setter
    @OneToOne(mappedBy = "user")
    private PersonalProfile personalProfile;

    public User(){

    }

    public User(String name, String surname, String email, String gender, Date datebirth, String login, String password, Role role) {

        this.login = login;
        this.password = password;
        this.role = role;
    }

    public User(User user) {

        this.login = user.getLogin();
        this.password = user.getPassword();
        this.role = user.getRole();
        this.predictions = user.getPredictions();
        this.predictionsCreated = user.getPredictionsCreated();
        this.personalProfile=user.getPersonalProfile();
        this.companyProfile=user.getCompanyProfile();
    }







}

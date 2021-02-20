package project.model;


import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Set;

@Entity
@Table(name="Roles")
public class Role implements Serializable {


    @Id
    @Column(name="name",nullable = false)
    private String name;

    @OneToMany(mappedBy = "role",orphanRemoval = true, cascade = CascadeType.PERSIST)
    @JsonIgnore
    private List<User> user;





    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<User> getUser() {
        return user;
    }

    public void setUser(List<User> user) {
        this.user = user;
    }


}

package project.DTO;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

public class RoleDTO implements Serializable {

    @Getter
    @Setter
    private String name;

    public RoleDTO(){

    }

    public RoleDTO(String name){
        this.name=name;

    }
}

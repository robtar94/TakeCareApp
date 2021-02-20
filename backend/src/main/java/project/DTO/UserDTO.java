package project.DTO;

import lombok.Getter;
import lombok.Setter;
import project.model.User;

import java.io.Serializable;
import java.util.Date;


public class UserDTO  implements Serializable {


    @Getter
    @Setter
    private int id;


    @Getter
    @Setter
    private String login;

    @Getter
    @Setter
    private String password;

    @Getter
    @Setter
    private RoleDTO roleDTO;


    public UserDTO(){

    }

    public UserDTO(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public UserDTO(UserDTO userDTO) {

        this.login = userDTO.getLogin();
        this.password = userDTO.getPassword();
        this.roleDTO= userDTO.getRoleDTO();
    }

    public UserDTO(User user) {

        this.id = user.getId();
        this.login = user.getLogin();
        this.roleDTO= new RoleDTO(user.getRole().getName());

    }





}

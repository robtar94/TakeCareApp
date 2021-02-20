package project.services;

import project.DTO.PersonalProfileDTO;
import project.DTO.UserDTO;
import project.model.Prediction;
import project.model.User;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

public interface UserService {

    public User addUser(User user);

    public Optional<User> getUsersByLogin(String login);
    public Optional<User> getUsersByLoginAndPassword(String login,String password);
    public void deleteUser(int id);
    public User getUserById(int id);
    public List<User> getAllUsers();
    public boolean checkIfCorrect(PersonalProfileDTO personalProfileDTO, boolean edit);



}

package project.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import project.DTO.PersonalProfileDTO;
import project.DTO.UserDTO;
import project.model.CustomUserDetails;
import project.model.PersonalProfile;
import project.model.Prediction;
import project.model.User;
import project.repositories.UserRepository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

import static org.springframework.security.web.context.HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY;

@Service
public class UserServiceImpl implements UserService,UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationManager authManager;

    public User addUser(User user){

        return this.userRepository.save(user);
    }

    public void deleteUser(int id){
        this.userRepository.deleteById(id);
    }

    public User getUserById(int id){
        return this.userRepository.findById(id);
    }

    public Optional<User> getUsersByLogin(String login){
        return this.userRepository.findByLogin(login);
    }

    public Optional<User> getUsersByLoginAndPassword(String login,String password){
        return this.userRepository.findByLoginAndPassword(login,password);
    }

    public List<User> getAllUsers(){
        return this.userRepository.findAll();
    }

    public List<SimpleGrantedAuthority> getAuthorities(User user){
        List<SimpleGrantedAuthority> authorityList = new ArrayList<SimpleGrantedAuthority>();
        authorityList.add(new SimpleGrantedAuthority(user.getRole().getName()));
        return authorityList;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        Optional<User> optionalUser = this.userRepository.findByLogin(s);
        if(optionalUser.isPresent()){
            return new org.springframework.security.core.userdetails.
                    User(optionalUser.get().getLogin(),
                    optionalUser.get().getPassword(),
                    getAuthorities(optionalUser.get()));

        }else{
            throw new UsernameNotFoundException("username not found");
        }

    }

    public boolean checkIfCorrect(PersonalProfileDTO personalProfileDTO, boolean edit){
        Pattern pName =Pattern.compile("^[A-Z][a-zA-ZĄąĆćĘęŁłŃńÓóŚśŹźŻż]{2,15}$");
        Pattern pSurname =Pattern.compile("^[A-Z][a-zA-ZĄąĆćĘęŁłŃńÓóŚśŹźŻż]{2,20}$");
        Pattern pEmail =Pattern.compile("^[a-z]+[0-9]*@([a-z]{2,10}\\.)+[a-z]{2,5}$");
        Pattern pLogin =Pattern.compile("^([a-zA-ZĄąĆćĘęŁłŃńÓóŚśŹźŻż]+[0-9\\-\\_]*){5,20}$");
        Pattern pPassword =Pattern.compile("^([a-zA-ZĄąĆćĘęŁłŃńÓóŚśŹźŻż]{5,}[0-9]{5,}[a-zA-ZĄąĆćĘęŁłŃńÓóŚśŹźŻż0-9]*)+$");

        if(!pName.matcher(personalProfileDTO.getPersonalDataDTO().getName()).matches() | !pSurname.matcher(personalProfileDTO.getPersonalDataDTO().getSurname()).matches() |
                !pEmail.matcher(personalProfileDTO.getPersonalDataDTO().getEmail()).matches() ) {

            return false;
        }else if(!edit){
            if(!pLogin.matcher(personalProfileDTO.getUserDTO().getLogin()).matches() |
                    !pPassword.matcher(personalProfileDTO.getUserDTO().getPassword()).matches()){
            return false;
            }else {
            return true;
            }
        }else{
            return true;
        }

    }



}

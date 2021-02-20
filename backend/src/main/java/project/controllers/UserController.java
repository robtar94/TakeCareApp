package project.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.type.filter.RegexPatternTypeFilter;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import project.DTO.PersonalDataDTO;
import project.DTO.PersonalProfileDTO;
import project.DTO.UserDTO;
import project.config.TokenHelper;
import project.model.*;
import project.services.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.*;
import java.util.regex.Pattern;


@CrossOrigin(origins = "http://localhost:4200")

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private GeneratorDTO generatorDTO;
    @Autowired
    private PersonalDataService personalDataService;

    @Autowired
    private PersonalProfileService personalProfileService;

    @Autowired
    private TokenHelper tokenHelper;


    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @PreAuthorize("isAnonymous()")
    @RequestMapping(value = "/register",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Map<String,Object>> addUser(@RequestBody @Valid @NotNull PersonalProfileDTO personalProfileDTO,
                                                      HttpServletRequest request,
                                                      HttpServletResponse httpServletResponse){

        ObjectMapper mapper = new ObjectMapper();


        Map<String,Object> map = new HashMap<>();

        Object token = request.getAttribute("token");
        map.put("token",token);

        Optional<User> users = this.userService.getUsersByLogin(personalProfileDTO.getUserDTO().getLogin());
        if(users.isPresent()){
            map.put("message","Wprowadzony użytkownik już istnieje");
            return new ResponseEntity<Map<String,Object>>(map, HttpStatus.BAD_REQUEST);
        }

        else if(!userService.checkIfCorrect(personalProfileDTO,false)) {
            map.put("message", "Niepoprawny login lub hasło");
            return new ResponseEntity<Map<String, Object>>(map, HttpStatus.UNAUTHORIZED);
        }

        else{

            User registerUser = new User();
            PersonalData personalData = new PersonalData();

            personalData.setName(personalProfileDTO.getPersonalDataDTO().getName());
            personalData.setSurname(personalProfileDTO.getPersonalDataDTO().getSurname());
            personalData.setEmail(personalProfileDTO.getPersonalDataDTO().getEmail());
            personalData.setGender(personalProfileDTO.getPersonalDataDTO().getGender());
            personalData.setDatebirth(personalProfileDTO.getPersonalDataDTO().getDatebirth());

            registerUser.setLogin(personalProfileDTO.getUserDTO().getLogin());

                String pass = this.bCryptPasswordEncoder.encode(personalProfileDTO.getUserDTO().getPassword());

                registerUser.setPassword(pass);

                Role role = this.roleService.getRoleByName(personalProfileDTO.getUserDTO().getRoleDTO().getName());
                registerUser.setRole(role);

                try {
                    User userSaved = this.userService.addUser(registerUser);
                    PersonalData personalDataSaved = this.personalDataService.addPersonalData(personalData);

                    PersonalProfile personalProfile = new PersonalProfile(personalDataSaved, userSaved);
                    this.personalProfileService.addPersonalProfile(personalProfile);

                    map.put("message", "Zostałeś pomyślnie zarejestrowany");
                    return new ResponseEntity<Map<String,Object>>(map, HttpStatus.OK);
                }catch (Exception e){
                    map.put("message", "Błąd rejestracji");

                    return new ResponseEntity<Map<String,Object>>(map, HttpStatus.BAD_REQUEST);
                }

        }

    }






    @PreAuthorize("hasAnyAuthority('COMP','IND')")
    @RequestMapping(value="/profile",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Map<String,Object>> getMyProfile(HttpServletRequest request,HttpServletResponse httpServletResponse){


        Map<String,Object> map = new HashMap<>();
        UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<User> user = userService.getUsersByLogin(userDetails.getUsername());

        if (user.isPresent()) {
            User currentUser = user.get();

            PersonalProfile personalProfile= personalProfileService.getPersonalProfileByUserId(currentUser.getId());


            Object token = request.getAttribute("token");

            map.put("token", tokenHelper.refreshToken(token.toString()));

            map.put("profil", this.generatorDTO.generatePersonalProfileDTO(personalProfile));
            return new ResponseEntity<>(map,HttpStatus.OK);

        }

        else {

            map.put("message", "Profil nie istnieje");
            return new ResponseEntity<>(map,HttpStatus.NOT_FOUND);

        }


    }

    @PreAuthorize("hasAnyAuthority('COMP','IND')")
    @RequestMapping(value="/profile",method = RequestMethod.PUT,produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Map<String,Object>> editMyProfile(@RequestBody @Valid @NotNull PersonalProfileDTO personalProfileDTO,HttpServletRequest request,HttpServletResponse httpServletResponse){

        Map<String,Object> map = new HashMap<>();
        UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<User> user = userService.getUsersByLogin(userDetails.getUsername());

        if (user.isPresent()) {
            User currentUser = user.get();

            PersonalProfile personalProfile = this.personalProfileService.getPersonalProfileByUserId(currentUser.getId());
            PersonalData personalData = personalProfile.getPersonalData();

            Object token = request.getAttribute("token");

            map.put("token", tokenHelper.refreshToken(token.toString()));

            if(userService.checkIfCorrect(personalProfileDTO,true)){

                PersonalDataDTO personalDataDTO = personalProfileDTO.getPersonalDataDTO();

                personalData.setName(personalDataDTO.getName());
                personalData.setSurname(personalDataDTO.getSurname());
                personalData.setDatebirth(personalDataDTO.getDatebirth());
                personalData.setGender(personalDataDTO.getGender());
                personalData.setEmail(personalDataDTO.getEmail());

                PersonalData personalDataSaved = personalDataService.addPersonalData(personalData);
                personalProfile.setPersonalData(personalDataSaved);

                PersonalProfile personalProfileSaved = personalProfileService.addPersonalProfile(personalProfile);
                map.put("profil", this.generatorDTO.generatePersonalProfileDTO(personalProfileSaved));
                return new ResponseEntity<>(map,HttpStatus.OK);

            }
            else{
                map.put("message", "Błędne dane");
                return new ResponseEntity<>(map,HttpStatus.BAD_REQUEST);
            }

        }

        else {

            map.put("message", "Profil nie istnieje");
            return new ResponseEntity<>(map,HttpStatus.NOT_FOUND);

        }


    }




}

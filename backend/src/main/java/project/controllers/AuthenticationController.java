package project.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import project.DTO.UserDTO;
import project.config.TokenHelper;

import project.model.CompanyProfile;
import project.model.PersonalProfile;
import project.model.User;
import project.services.CompanyProfileService;
import project.services.PersonalProfileService;
import project.services.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    TokenHelper tokenHelper;

    @Autowired
    UserService userService;

    @Autowired
    PersonalProfileService personalProfileService;

    @Autowired
    CompanyProfileService companyProfileService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @PreAuthorize("isAnonymous()")
    @RequestMapping(value = "/login",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Map<String,Object>> generateToken(@RequestBody UserDTO loginForm,
                                                            HttpServletRequest request,
                                                            HttpServletResponse httpServletResponse){


        String token=null;
        Map<String,Object> map = new HashMap<>();


        Optional<User> userFromDatabase = this.userService.getUsersByLogin(loginForm.getLogin());

         Authentication authentication =null;


        if(userFromDatabase.isPresent()){

            authentication = this.authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(loginForm.getLogin(),loginForm.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);

            User authUser = userFromDatabase.get();
            token = this.tokenHelper.generateToken(authUser);
            map.put("token",token);
            if(authUser.getRole().getName()=="COMP"){
                CompanyProfile companyProfile = companyProfileService.getCompanyProfileByUserId(authUser.getId());
                map.put("message","Jesteś zalogowany jako "+companyProfile.getCompanyData().getName());

            }else if(authUser.getRole().getName()=="IND"){
                PersonalProfile personalProfile = personalProfileService.getPersonalProfileByUserId(authUser.getId());
                map.put("message","Jesteś zalogowany jako "+personalProfile.getPersonalData().getName()+" "+personalProfile.getPersonalData().getSurname());

            }

            return new ResponseEntity<>(map, HttpStatus.OK);
        }
            else{
            map.put("message","Niepoprawne login lub hasło");
            map.put("token",token);
            return new ResponseEntity<>(map, HttpStatus.UNAUTHORIZED);

        }



    }

}

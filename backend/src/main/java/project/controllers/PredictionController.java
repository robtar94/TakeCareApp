package project.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import project.DTO.PredictionDTO;
import project.config.TokenHelper;
import project.model.CompanyProfile;
import project.model.PersonalProfile;
import project.model.Prediction;
import project.model.User;
import project.services.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/prediction")
public class PredictionController {


    @Autowired
    private PredictionService predictionService;
    @Autowired
    private UserService userService;
    @Autowired
    private PersonalProfileService personalProfileService;

    @Autowired
    private CompanyProfileService companyProfileService;

    @Autowired
    private GeneratorDTO generatorDTO;

    @Autowired
    private TokenHelper tokenHelper;




    @PreAuthorize("hasAnyAuthority('COMP','IND')")
    @RequestMapping(value = "/save",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Map<String,Object> addPrediction(@RequestBody @Valid @NotNull PredictionDTO predictionDTO,
                                            HttpServletRequest request,
                                            HttpServletResponse httpServletResponse){


        Map<String,Object> map = new HashMap<>();
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<User> user = userService.getUsersByLogin(userDetails.getUsername());

        if (user.isPresent()) {
            User currentUser = user.get();

            Object token = request.getAttribute("token");

            map.put("token", tokenHelper.refreshToken(token.toString()));

            if(currentUser.getRole().getName()=="COMP"){
                CompanyProfile companyProfile = companyProfileService.getCompanyProfileByUserId(currentUser.getId());
                PersonalProfile personalProfile = personalProfileService.getPersonalProfileByUserId(predictionDTO.getUser());
                if(!companyProfileService.isCompanyAuthorized(companyProfile.getId(),personalProfile.getId())){
                    httpServletResponse.setStatus(400);
                }else{
                    predictionDTO.setUser(predictionDTO.getUser());
                    predictionDTO.setCreator(currentUser.getId());
                }

            }else{
                predictionDTO.setUser(currentUser.getId());
                predictionDTO.setCreator(currentUser.getId());
            }



            Prediction predictionSaved = this.predictionService.savePrediction(predictionDTO);
            if (predictionSaved!=null){
                map.put("prediction", new PredictionDTO(predictionSaved));
                httpServletResponse.setStatus(200);
            }else{
                map.put("prediction", null);
                httpServletResponse.setStatus(400);
            }

        }

        else {

            map.put("message", "Błąd autoryzacji");
            httpServletResponse.setStatus(401);

        }
        return map;
    }

    @PreAuthorize("hasAnyAuthority('COMP','IND')")
    @RequestMapping(value = "/get/{id}",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String,Object>> getPrediction(@PathVariable int id,
                                        HttpServletRequest request,
                                        HttpServletResponse httpServletResponse){


        Map<String,Object> map = new HashMap<>();
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<User> user = userService.getUsersByLogin(userDetails.getUsername());

        if (user.isPresent()) {
            User currentUser = user.get();

            Object token = request.getAttribute("token");

            map.put("token", tokenHelper.refreshToken(token.toString()));

            Prediction prediction = this.predictionService.getPrediction(id);
            if (prediction!=null){
                if(prediction.getUser().getId()==currentUser.getId() || prediction.getCreator().getId()==currentUser.getId()){
                    map.put("prediction", new PredictionDTO(prediction));
                    return new ResponseEntity<Map<String,Object>>(map, HttpStatus.OK);
                }else{
                    map.put("prediction", null);
                    return new ResponseEntity<Map<String,Object>>(map, HttpStatus.BAD_REQUEST);
                }

            }else{
                map.put("prediction", null);
                return new ResponseEntity<Map<String,Object>>(map, HttpStatus.BAD_REQUEST);
            }

        }

        else {

            map.put("message", "Błąd autoryzacji");
            return new ResponseEntity<Map<String,Object>>(map, HttpStatus.UNAUTHORIZED);

        }

    }

    @PreAuthorize("hasAnyAuthority('COMP','IND')")
    @RequestMapping(value = "delete/{id}",method = RequestMethod.DELETE,produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String,Object>> deletePrediction(@PathVariable int id,HttpServletRequest request,
                                                                  HttpServletResponse httpServletResponse){

        Map<String,Object> map = new HashMap<>();
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<User> user = userService.getUsersByLogin(userDetails.getUsername());

        if (user.isPresent()) {
            User currentUser = user.get();

            Object token = request.getAttribute("token");

            map.put("token", tokenHelper.refreshToken(token.toString()));


            boolean isDeleted = this.predictionService.deletePrediction(currentUser.getId(),id);
            if (isDeleted){

                return new ResponseEntity<Map<String,Object>>(map, HttpStatus.OK);
            }else{

                return new ResponseEntity<Map<String,Object>>(map, HttpStatus.BAD_REQUEST);
            }

        }

        else {

            map.put("message", "Błąd autoryzacji");
            return new ResponseEntity<Map<String,Object>>(map, HttpStatus.UNAUTHORIZED);

        }

    }


    @PreAuthorize("hasAnyAuthority('IND')")
    @RequestMapping(value = "/usersPredictions/ind",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String,Object>> getUsersPredictions(HttpServletRequest request,
                                                            HttpServletResponse httpServletResponse){

        Map<String,Object> map = new HashMap<>();
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<User> user = userService.getUsersByLogin(userDetails.getUsername());

        if (user.isPresent()) {
            User currentUser = user.get();

            Object token = request.getAttribute("token");

            map.put("token", tokenHelper.refreshToken(token.toString()));

            List<Prediction> predictions = this.predictionService.getAllPredictionsForUser(currentUser.getId());
            if (predictions!=null){
                map.put("predictions", this.generatorDTO.generatePredictionsDTO(predictions));
                return new ResponseEntity<Map<String,Object>>(map, HttpStatus.OK);
            }else{
                map.put("predictions", null);
                return new ResponseEntity<Map<String,Object>>(map, HttpStatus.BAD_REQUEST);
            }

        }

        else {

            map.put("message", "Błąd autoryzacji");
            return new ResponseEntity<Map<String,Object>>(map, HttpStatus.UNAUTHORIZED);

        }


    }



}

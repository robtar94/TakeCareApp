package project.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import project.DTO.CompanyProfileDTO;
import project.DTO.PredictionDTO;
import project.model.CompanyProfile;
import project.services.CompanyProfileService;
import project.services.GeneratorDTO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/firms")
public class CompanyProfileController {
    @Autowired
    private CompanyProfileService companyProfileService;

    @Autowired
    private GeneratorDTO generatorDTO;


    @PreAuthorize("isAnonymous()")
    @RequestMapping(value = "/all",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Map<String,Object>> getAllCompanies(HttpServletRequest request,
                                                            HttpServletResponse httpServletResponse){

        Map<String,Object> map = new HashMap<>();
//        map.put("companies",new Integer(1));
//        return new ResponseEntity<>(map, HttpStatus.OK);
        try{
            List<CompanyProfileDTO> companyProfileDTOS = new ArrayList<>();
            for (CompanyProfile cp:companyProfileService.getAllCompanies()) {
                companyProfileDTOS.add(generatorDTO.generateCompanyProfileDTO(cp));

            }
            map.put("companies",companyProfileDTOS);
            return new ResponseEntity<>(map, HttpStatus.OK);
        }catch(Exception e){
            map.put("companies",null);
            map.put("message","Błąd zapytania");
            return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
        }


    }
}

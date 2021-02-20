package project.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ControllersExceptionHandler {

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String,Object>> handle(){
        Map<String,Object> map = new HashMap<>();
        map.put("message","Wprowadzono niepoprawny typ danych");

        return new ResponseEntity<Map<String,Object>>(map,HttpStatus.BAD_REQUEST);
    }
}

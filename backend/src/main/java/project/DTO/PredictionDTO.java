package project.DTO;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import project.model.ParameterDouble;
import project.model.ParameterInt;
import project.model.Prediction;
import project.model.User;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

public class PredictionDTO implements Serializable {

    @Getter
    @Setter
    private int id;

    @Getter
    @Setter
    private String name;

    @Getter
    @Setter
    private int user;

    @Getter
    @Setter
    private int creator;

    @Getter
    @Setter
    private List<ParameterDouble> parameterDoubles;

    @Getter
    @Setter
    private List<ParameterInt> parameterInts;

    @Getter
    @Setter
    private double resultValue;

    @Getter
    @Setter
    private String resultText;


    @Getter
    @Setter
    private LocalDateTime localDateTime;

    public PredictionDTO(){};

    public PredictionDTO(PredictionDTO predictionDTO){
        this.id = predictionDTO.getId();
        this.name = predictionDTO.getName();
        this.user = predictionDTO.getUser();
        this.creator = predictionDTO.getCreator();
        this.parameterDoubles = predictionDTO.getParameterDoubles();
        this.parameterInts = predictionDTO.getParameterInts();
        this.resultValue = predictionDTO.getResultValue();
        this.resultText = predictionDTO.getResultText();
        this.localDateTime = predictionDTO.getLocalDateTime();


    }

    public PredictionDTO(Prediction prediction){
        this.id = prediction.getId();
        this.name = prediction.getName();
        this.user = prediction.getUser().getId();
        this.creator = prediction.getCreator().getId();
        this.parameterDoubles = prediction.getParameterDoubles();
        this.parameterInts = prediction.getParameterInts();
        this.resultValue = prediction.getResultValue();
        this.resultText = prediction.getResultText();
        this.localDateTime = prediction.getLocalDateTime();


    }

}

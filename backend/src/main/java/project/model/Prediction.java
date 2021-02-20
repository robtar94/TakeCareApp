package project.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import project.DTO.PredictionDTO;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Entity
@Table(name="Prediction")
public class Prediction implements Serializable {

    @Getter
    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id",nullable = false)
    private int id;

    @Getter
    @Setter
    @Column(name="name",nullable=false)
    private String name;

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "creator_id", referencedColumnName = "id")
    private User creator;


    @Getter
    @Setter
    @OneToMany(mappedBy = "prediction",orphanRemoval = true, cascade = CascadeType.PERSIST)
    @JsonIgnore
    private List<ParameterDouble> parameterDoubles;


    @Getter
    @Setter
    @OneToMany(mappedBy = "prediction",orphanRemoval = true, cascade = CascadeType.PERSIST)
    @JsonIgnore
    private List<ParameterInt> parameterInts;

    @Getter
    @Setter
    @Column(name="resultValue",nullable = false)
    private double resultValue;

    @Getter
    @Setter
    @Column(name="date",nullable = false)
    private LocalDateTime localDateTime;


    @Getter
    @Setter
    @Column(name="resultText",nullable = false)
    private String resultText;

    public Prediction(){};
    public Prediction(PredictionDTO predictionDTO){
        this.name = predictionDTO.getName();
        this.parameterDoubles = predictionDTO.getParameterDoubles();
        this.parameterInts = predictionDTO.getParameterInts();
        this.resultValue = predictionDTO.getResultValue();
        this.resultText = predictionDTO.getResultText();
    }



}

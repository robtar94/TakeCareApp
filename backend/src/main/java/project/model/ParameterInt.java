package project.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name="ParameterInt")
public class ParameterInt implements Serializable {

    @Getter
    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id",nullable=false)
    private int id;

    @Getter
    @Setter
    @Column(name="value",nullable=false)
    private int value;

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name="prediction",referencedColumnName = "id")
//    @JsonIgnoreProperties({"name", "user","parameterDoubles","parameterInts","resultValue","resultText"})
    @JsonIgnore
    private Prediction prediction;

    public ParameterInt(int value){
        this.value=value;
    }

    public ParameterInt(){}

}

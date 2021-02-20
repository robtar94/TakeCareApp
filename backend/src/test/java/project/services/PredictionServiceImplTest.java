package project.services;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import project.DTO.PredictionDTO;
import project.DTO.UserDTO;
import project.model.ParameterDouble;
import project.model.ParameterInt;
import project.model.Prediction;
import project.repositories.PredictionRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PredictionServiceImplTest {

    @Autowired
    private PredictionServiceImpl predictionService;


    public PredictionDTO generatePredictionDTO(){
        PredictionDTO predictionDTO = new PredictionDTO();
        predictionDTO.setName("Przykład");
        predictionDTO.setUser(1);
        List<ParameterDouble> parameterDoubles = new ArrayList<>();
        List<ParameterInt> parameterInts = new ArrayList<>();
        double[] tableDouble = {1.2,1.5,8.5,6.3,9.9,25.1};
        int[] tableInt = {7,54,3,6,8,4,33,66,85,44,1001};
        for (double x:tableDouble) {
            parameterDoubles.add(new ParameterDouble(x));
        }
        for (int x:tableInt) {
            parameterInts.add(new ParameterInt(x));
        }
        predictionDTO.setParameterInts(parameterInts);
        predictionDTO.setParameterDoubles(parameterDoubles);
        predictionDTO.setResultValue(0.98);
        predictionDTO.setResultText("Otrzymany wynik świadczy o b. wysokim prawdopodobieństwie zachorowalności.");

        return predictionDTO;
    }

    @Test
    public void saveTest(){

        PredictionDTO predictionDTO = generatePredictionDTO();

        Prediction prediction = predictionService.savePrediction(predictionDTO);
        System.out.println(prediction.getParameterInts());
        Assert.assertTrue(prediction!=null);
    }

    @Test
    public void getTest(){
        Prediction prediction = predictionService.getPrediction(8);
        Assert.assertTrue(prediction.getName()!=null);
        Assert.assertTrue(prediction.getUser()!=null);
        Assert.assertTrue(prediction.getParameterDoubles()!=null);
        Assert.assertTrue(prediction.getParameterInts()!=null);
        Object d = prediction.getResultValue();
        Object t = prediction.getResultText();
        Assert.assertTrue(d instanceof Double);
        Assert.assertTrue(t instanceof String);


    }

    @Test
    public void getAllTest(){
        List<Prediction> predictions = predictionService.getAllPredictionsForUser(1);
        Assert.assertTrue(predictions!=null);
        Assert.assertTrue(predictions.size()!=0);

    }


}

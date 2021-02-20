package project.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.DTO.PredictionDTO;
import project.model.ParameterDouble;
import project.model.ParameterInt;
import project.model.Prediction;
import project.repositories.ParameterDoubleRepository;
import project.repositories.ParameterIntRepository;
import project.repositories.PredictionRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PredictionServiceImpl implements PredictionService {

    @Autowired
    private PredictionRepository predictionRepository;

    @Autowired
    private ParameterIntRepository parameterIntRepository;

    @Autowired
    private ParameterDoubleRepository parameterDoubleRepository;

    @Autowired
    private UserServiceImpl userService;

    public Prediction savePrediction(PredictionDTO predictionDTO){
        Prediction prediction = new Prediction(predictionDTO);
        prediction.setParameterDoubles(null);
        prediction.setParameterInts(null);

        prediction.setLocalDateTime(LocalDateTime.now());

        prediction.setUser(userService.getUserById(predictionDTO.getUser()));
        prediction.setCreator(userService.getUserById(predictionDTO.getCreator()));
        Prediction savedPrediction = this.predictionRepository.save(prediction);

        List<ParameterInt> parameterInts = new ArrayList<>();
        List<ParameterDouble> parameterDoubles = new ArrayList<>();

        List<ParameterInt> parameterIntsDTO = predictionDTO.getParameterInts();
        List<ParameterDouble> parameterDoublesDTO = predictionDTO.getParameterDoubles();
        if(parameterIntsDTO !=null){
            for (ParameterInt x:predictionDTO.getParameterInts()) {
                x.setPrediction(savedPrediction);
                parameterInts.add(parameterIntRepository.save(x));
            }
        }

        savedPrediction.setParameterInts(parameterInts);

        if(parameterDoublesDTO != null){
            for (ParameterDouble x:predictionDTO.getParameterDoubles()) {
                x.setPrediction(savedPrediction);
                parameterDoubles.add(parameterDoubleRepository.save(x));
            }
        }


        savedPrediction.setParameterDoubles(parameterDoubles);
//        return prediction;
        return this.predictionRepository.save(savedPrediction);


    }
    public Prediction getPrediction(int id){
        Optional<Prediction> predictionOptional= this.predictionRepository.findById(id);
        if(predictionOptional.isPresent()){
            return predictionOptional.get();
        }
        return null;
    }

    public boolean deletePrediction(int userId,int id){
        Prediction pred = getPrediction(id);
        if(pred.getCreator().getId()==userId){
            this.predictionRepository.deleteById(id);
            return true;
        }
        return false;

    }
    public List<Prediction> getAllPredictionsForUser(int userId){
        Optional<List<Prediction>> optionalPredictions = predictionRepository.findByUserId(userId);
        if(optionalPredictions.isPresent()){
            return optionalPredictions.get();
        }
        return null;
    }

}

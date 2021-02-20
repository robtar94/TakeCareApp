package project.services;

import project.DTO.PredictionDTO;
import project.model.Prediction;

import java.util.List;

public interface PredictionService {

    public Prediction savePrediction(PredictionDTO predictionDTO);
    public Prediction getPrediction(int id);
    public List<Prediction> getAllPredictionsForUser(int userId);
    public boolean deletePrediction(int userId,int id);
}

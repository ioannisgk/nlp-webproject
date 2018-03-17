package com.ioannisgk.nlpwebproject.services;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.nnet.learning.BackPropagation;
import org.neuroph.util.TransferFunctionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

////////////////////////////////////////////////////////////////////////////////////////////////////////////
//                                                                                                        //
// CREATE METHODS(2): CREATE AN MLP NEURAL NETWORK AND TRAIN IT BASED ON USER SETTINGS                    //
// TEST METHODS(2): TEST A DATASET AND A USER MESSAGE AND GET CATEGORY AND PROBABILITY                    //
// HELPER METHODS: GET RANDOM INTEGER, CLASS GETTERS AND SETTERS                                          //
//                                                                                                        //
// NOTE: This class accesses and processes data from the DatasetService class.                            //
// LINK: https://github.com/neuroph/neuroph (Used Neuroph framework to create the MLP, train and test it) //
//                                                                                                        //
////////////////////////////////////////////////////////////////////////////////////////////////////////////

@Service
public class NeuralNetworkService {
	
	// Inject the dataset service
	@Autowired
	private DatasetService datasetService;
	
	// Class attributes
	
	private String mode = "";
	private int layer1Size = 0;
	private int layer2Size = 0;
	private int layer3Size = 0;
	private int iterations = 0;
	
	//////////////////////////////////////////////////////////////////////////////////////////
	//                                                                                      //
	// CREATE METHODS(2): Create an MLP neural network and train it based on user settings. //
	//                                                                                      //
	//////////////////////////////////////////////////////////////////////////////////////////
	
	// Method to create an MLP neural network based on user settings
	public MultiLayerPerceptron createMultiLayerPerceptron() {
		
		MultiLayerPerceptron mlp = null;
		
		// Get number of input and output neurons
		
		int inputNeurons = datasetService.getInputNeurons();
		int outputNeurons = datasetService.getOutputNeurons();
		
		if (mode.equals("untrained")) {
			
			// If mode is untrained, set sigmoid transfer function, input and output neurons
			mlp = new MultiLayerPerceptron(TransferFunctionType.SIGMOID, inputNeurons, outputNeurons);
			
			// Set back propagation as a learning rule
			mlp.setLearningRule(new BackPropagation());
			
		} else if (mode.equals("mlp-01layer")) {
			
			// If mode is mlp-01layer, set sigmoid transfer function, input and output neurons and layer1 size
			mlp = new MultiLayerPerceptron(TransferFunctionType.SIGMOID, inputNeurons, layer1Size, outputNeurons);
			
			// Set back propagation as a learning rule
			mlp.setLearningRule(new BackPropagation());
			
		} else if (mode.equals("mlp-02layer")) {
			
			// If mode is mlp-02layer, set sigmoid transfer function, input and output neurons, layer1 and layer2 sizes
			mlp = new MultiLayerPerceptron(TransferFunctionType.SIGMOID, inputNeurons, layer1Size, layer2Size, outputNeurons);
			
			// Set back propagation as a learning rule
			mlp.setLearningRule(new BackPropagation());
			
		} else if (mode.equals("mlp-03layer")) {
			
			// If mode is mlp-03layer, set sigmoid transfer function, input and output neurons, layer1, layer2 and layer3 sizes
			mlp = new MultiLayerPerceptron(TransferFunctionType.SIGMOID, inputNeurons, layer1Size, layer2Size, layer3Size, outputNeurons);
			
			// Set back propagation as a learning rule
			mlp.setLearningRule(new BackPropagation());
		}
		
		return mlp;
	}
	
	// Method to train the MLP neural network
	public MultiLayerPerceptron trainMultiLayerPerceptron(MultiLayerPerceptron mlp, DataSet trainingSet) {
		
		for (int i = 0; i < iterations; i++) {
			mlp.learn(trainingSet);
		}
		
		return mlp;
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////
	//                                                                                           //
	// TEST METHODS(2): Test a test dataset and get overall probability and correct predictions, //
	// test a the user message on the neural network and get selected category and probability.  //
	//                                                                                           //
	///////////////////////////////////////////////////////////////////////////////////////////////
	
	// Method to test the neural network on a test dataset and get overall probability and correct predictions
	public Map<String, String> testNeuralNetwork(NeuralNetwork nnet, DataSet testSet) {
		
		int i = 5;
		int counter = 0;
		double correctPredictions = 0;
		double sum = 0.0;
		double probability = 0.0;
		Map<String, String> resultsMap = new HashMap<>();
		
		for (DataSetRow dataRow : testSet.getRows()) {
			
			nnet.setInput(dataRow.getInput());
			nnet.calculate();
			double[] networkOutput = nnet.getOutput();
			
			// System.out.print("Input: " + Arrays.toString(dataRow.getInput()));
			// System.out.println(" Output: " + Arrays.toString(networkOutput));
			
			// Get all categories as a list
			List<String> datasetResultList = datasetService.getAllResultsList();
			
			// Get selected position by the neural network
			int selectedPosition = getIndexOfMax(networkOutput);
			
			// Get selected category
			String selectedCategory = datasetResultList.get(selectedPosition);
			
			// Get probability for selected category
			probability = networkOutput[selectedPosition];
			
			// System.out.print("Assigned Category: " + selectedCategory);
			// System.out.println("Probability %: " + probability * 100);
			
			if (counter == i) i = i + 5;
			
			if (counter < i) {
				
				// Get number of correct predictions
				if (selectedPosition == ((i / 5) - 1)) correctPredictions++;
			}
			
			counter++;
			sum = sum + probability;
		}
		
		// System.out.println("Overall probability %: " + (sum * 100)/counter);
		// System.out.println("Correct predictions %: " + (correctPredictions * 100)/counter);
		
		// Save overall probability and correct predictions on the map
		
		resultsMap.put("overallProbability", Double.toString((sum * 100)/counter));
		resultsMap.put("correctPredictions", Double.toString((correctPredictions * 100)/counter));
		
		return resultsMap;
	}
	
	// Method to test the neural network on a the user message and get selected category and probability
	public Map<String, String> testNeuralNetwork(NeuralNetwork nnet, String message) {
		
		// Get number of input and output neurons
		
		int inputNeurons = datasetService.getInputNeurons();
		int outputNeurons = datasetService.getOutputNeurons();
		
		// Create dataset
		DataSet testSet = new DataSet(inputNeurons, outputNeurons);
		
		// Add custom array and result rows to the dataset
		
		double[] customDataset = datasetService.createCustomDatasetBinary(message);
		double[] resultSet = datasetService.createResultDatasetBinaryList().get(0);
		
		// Add rows to the dataset
		testSet.addRow(new DataSetRow(customDataset, resultSet));
		
		double probability = 0.0;
		Map<String, String> resultsMap = new HashMap<>();
		
		for (DataSetRow dataRow : testSet.getRows()) {
			
			nnet.setInput(dataRow.getInput());
			nnet.calculate();
			double[] networkOutput = nnet.getOutput();
			
			// System.out.print("Input: " + Arrays.toString(dataRow.getInput()));
			// System.out.println(" Output: " + Arrays.toString(networkOutput));
			
			// Get all categories as a list
			List<String> datasetResultList = datasetService.getAllResultsList();
			
			// Get selected position by the neural network
			int selectedPosition = getIndexOfMax(networkOutput);
			
			// Get selected category
			String selectedCategory = datasetResultList.get(selectedPosition);
			
			// Get probability for selected category
			probability = networkOutput[selectedPosition];
			
			// System.out.print("Assigned Category: " + selectedCategory);
			// System.out.println("Probability %: " + probability * 100);
			
			// Save selected category and probability on the map
			
			resultsMap.put("selectedCategory", selectedCategory);
			resultsMap.put("probability", Double.toString(probability * 100));
		}
		
		return resultsMap;
	}
	
	////////////////////////////////////////////////////////////////////
	//                                                                //
	// HELPER METHODS: Get random integer, class getters and setters. //
	//                                                                //
	////////////////////////////////////////////////////////////////////
	
	// Method to get random integer based on min and max values
	private int getIndexOfMax(double[] output) {
	    
	    double max = output[0];
	    int pos = 0;

	    for (int i = 1; i < output.length; i++) {
	    	
	        if (max < output[i]) {
	            pos = i;
	            max = output[i];
	        }
	    }
	    return pos;
	}
	
	// Getters and setters
	
	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}
	
	public int getLayer1Size() {
		return layer1Size;
	}

	public void setLayer1Size(int layer1Size) {
		this.layer1Size = layer1Size;
	}
	
	public int getLayer2Size() {
		return layer2Size;
	}
	
	public void setLayer2Size(int layer2Size) {
		this.layer2Size = layer2Size;
	}
	
	public int getLayer3Size() {
		return layer3Size;
	}
	
	public void setLayer3Size(int layer3Size) {
		this.layer3Size = layer3Size;
	}
	
	public int getIterations() {
		return iterations;
	}
	
	public void setIterations(int iterations) {
		this.iterations = iterations;
	}
}
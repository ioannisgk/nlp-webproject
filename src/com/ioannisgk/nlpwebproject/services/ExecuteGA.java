package com.ioannisgk.nlpwebproject.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ioannisgk.nlpwebproject.controllers.HomeController;

////////////////////////////////////////////////////////////////////////////////////////////////////////////
//                                                                                                        //
// EVOLVE METHOD(1): CREATE A NEW POPULATION, APPLY ELIISM, CROSSOVER, AND MUTATE THE POPULATION (EVOLVE) //
// HELPER METHODS(5): HELPER METHODS TO SUPPORT THE POPULATION EVOLUTION                                  //
// METHODS(5): DECODE INDIVIDUALS INTO MLP PARAMETERS, TRAIN AND TEST MLP TO GET FITNESS OF INDIVIDUAL    //
//                                                                                                        //
// NOTE: This class executes a Genetic Algorithm to find the best settings for the MLP training.          //
//                                                                                                        //
////////////////////////////////////////////////////////////////////////////////////////////////////////////

@Service
public class ExecuteGA {
	
	// Inject the neural network service
	@Autowired
	private NeuralNetworkService neuralNetworkService;
	
	// Inject the dataset service
	@Autowired
	private DatasetService datasetService;
	
	// Inject the home controller
	@Autowired
	private HomeController homeController;
	
	private static final int populationSize = 8;
	private static final int individualLength = 10;
	private static final double uniformRate = 0.5;
    private static final double mutationRate = 0.2;
    private static final int tournamentSize = 2;
	private static final boolean elitism = true;
	private static final int allocationFactor = 8;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//                                                                                                                //
	// EVOLVE METHOD(1): Create a new population, keep the best individual, select parents with tournament selection, //
	// crossover parents, get a child and add it to the new population, repeat, mutate the new population.            //
	//                                                                                                                //
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	// Method to evolve the population
	public List<int[]> evolvePopulation(List<int[]> population) {
		
		List<int[]> newPopulation = new ArrayList<int[]>();
		int[] bestIndividual = getFittest(population);
		
		System.out.println("Step 1: Best individual selected");
		
		int elitismOffset;
        if (elitism) elitismOffset = 1; else elitismOffset = 0;
		
		// Keep the best individual
        if (elitism) newPopulation.add(bestIndividual);
        
        // Iterate the population and create new individuals with crossover
        
        for (int i = elitismOffset; i < populationSize; i++) {
        	
        	// Select parent individuals with tournament method
        	
        	int[] parent1 = tournamentSelection(population);
        	
        	System.out.println("Step 2a: Parent 1 selected");
        	
        	int[] parent2 = tournamentSelection(population);
        	
        	System.out.println("Step 2b: Parent 2 selected");
            
            // Crossover individuals
        	int[] child = crossover(parent1, parent2);
            
            // Save child individual to new population
        	newPopulation.add(child);
        	
        	System.out.println("Step 2c: Child added");
        }

        // Mutate population
        newPopulation = mutate(newPopulation);
        
        System.out.println("Step 3: Population mutated");
        
		return population;
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////////
	//                                                                                               //
	// HELPER METHODS(5): Create a new population with individuals, select individuals for crossover //
	// with tournament selection, crossover individuals, mutate individuals, get fittest individual. //
	//                                                                                               //
	///////////////////////////////////////////////////////////////////////////////////////////////////
	
    // Method to create a new population with individuals
    public List<int[]> createPopulation() {
		
		List<int[]> population = new ArrayList<int[]>();
		
		for (int i = 0; i < populationSize; i++) {
			
			int[] individual = new int[individualLength];
			
			for (int j = 0; j < individualLength; j++) {
				individual[j] = randInt(0, 1);
			}
			population.add(individual);
		}
		return population;
	}
    
    // Method to select individuals for crossover with tournament selection
    private int[] tournamentSelection(List<int[]> population) {
    	
        // Create a tournament population
    	List<int[]> tournament = new ArrayList<int[]>();
        
        // For each place in the tournament get a random individual
        
        for (int i = 0; i < tournamentSize; i++) {
            
            int[] individual = population.get(randInt(0, populationSize - 1));
            tournament.add(individual);
        }
        
        // Get the fittest individual from the tournament
        int[] fittest = getFittest(tournament);
        
        return fittest;
    }
	
	// Method to crossover individuals
    private int[] crossover(int[] parent1, int[] parent2) {
    	
    	int[] child = new int[individualLength];
        
        for (int i = 0; i < individualLength; i++) {
        	
            // Inherit characteristics from both parents
        	
            if (Math.random() <= uniformRate) child[i] = parent1[i];
            else child[i] = parent2[i];
        }
        return child;
    }

    // Method to mutate an individual
    private List<int[]> mutate(List<int[]> population) {
    	
    	int elitismOffset;
        if (elitism) elitismOffset = 1; else elitismOffset = 0;
        
        List<int[]> newPopulation = new ArrayList<int[]>();
        
    	for (int i = elitismOffset; i < populationSize; i++) {
			
			int[] individual = population.get(i);
			
			for (int j = 0; j < individualLength; j++) {
				
				if (Math.random() <= mutationRate) {
					individual[j] = randInt(0, 1);
	            }
			}
			newPopulation.add(individual);
		}
        return newPopulation;
    }
    
    // Method to get the fittest individual from a population
	public int[] getFittest(List<int[]> population) {
		
		int[] fittest = population.get(0);
        
        for (int i = 1; i < population.size(); i++) {
			
        	double currentFitness = getFitness(population.get(i));
        	
			if (currentFitness > getFitness(fittest)) {
				
				for (int j = 0; j < individualLength; j++) {
					fittest[j] = population.get(i)[j];
				}
			}
		}     
        
        System.out.println("Got fittest!");
        
        return fittest;
	}
	
	//////////////////////////////////////////////////////////////////////////////////////////
	//                                                                                      //
	// METHODS(5): Get the fitness of an individual: decode individual into MLP parameters, //
	// create a parameter map to add MLP parameters, train and test MLP to get fitness.     //
	// Get allocation factor, calculate random integer with min and max.                    //
	//                                                                                      //
	//////////////////////////////////////////////////////////////////////////////////////////
	
	// Method to get the fitness of an individual
	public double getFitness(int[] individual) {
		
		// Decode individual into mlp parameters
		
		Map<String, String> individualResultsMap = getIndividualResultsMap( individual);
		
		int numberOfLayers = Integer.parseInt(individualResultsMap.get("numberOfLayers"));
		int numberOfNeurons = Integer.parseInt(individualResultsMap.get("numberOfNeurons"));
		int numberOfIterations = Integer.parseInt(individualResultsMap.get("numberOfIterations"));
		
		// Create a parameter map to add mlp parameters
		Map<String, String[]> parameterMap = getParameterMap(numberOfLayers, numberOfNeurons, numberOfIterations);
		
		// Create an MLP neural network based on parameters
		MultiLayerPerceptron mlp = homeController.createMlp(parameterMap);
		
		// Create training dataset using the service
		DataSet trainingSet = datasetService.getTrainingDataset();
		
		// Train the neural network using the service
		mlp = neuralNetworkService.trainMultiLayerPerceptron(mlp, trainingSet);
		
		// Save trained neural network on file
		mlp.save("/opt/tomcat/tomcat-latest/mlp.nnet");
		
		// Load trained neural network from file
		NeuralNetwork nnet = NeuralNetwork.createFromFile("/opt/tomcat/tomcat-latest/mlp.nnet");
		
		// Create test dataset using the service
		DataSet testSet = datasetService.getTestDataset();
		
		// Test the neural network on the test dataset and get overall probability and correct predictions map
		Map<String, String> resultsMap = neuralNetworkService.testNeuralNetwork(nnet, testSet);
		
		// Get fitness as the correct predictions percentage
		double fitness = Double.parseDouble(resultsMap.get("correctPredictions"));
		
		System.out.println("Got fitness: " + fitness + "%");
		
		return fitness;
	}
	
	// Method to decode individual into mlp parameters
	public Map<String, String> getIndividualResultsMap(int[] individual) {
		
		Map<String, String> resultsMap = new HashMap<String, String>();
		
		String numberOfLayersString = "";
		String numberOfNeuronsString = "";
		String numberOfIterationsString = "";
		
		// Decode individual into three string variables
		
		for (int j = 0; j < individualLength - 8; j++) {
			numberOfLayersString = numberOfLayersString + individual[j];
		}
		
		for (int j = 2; j < individualLength - 4; j++) {
			numberOfNeuronsString = numberOfNeuronsString + individual[j];
		}
		
		for (int j = 6; j < individualLength; j++) {
			numberOfIterationsString = numberOfIterationsString + individual[j];
		}
		
		// Convert individual string to its three decimal representations
		
		int numberOfLayers = Integer.parseInt(numberOfLayersString , 2);
		int numberOfNeurons = Integer.parseInt(numberOfNeuronsString, 2);
		int numberOfIterations = Integer.parseInt(numberOfIterationsString, 2);
		
		// Make sure the variables do not get a value of 0, since it is invalid for the mlp
		
		if (numberOfLayers == 0) numberOfLayers = randInt(1, 3);
		if (numberOfNeurons == 0) numberOfNeurons = randInt(1, 15);
		if (numberOfIterations == 0) numberOfIterations = randInt(1, 15);
		
		// Add variables to results map
		
		resultsMap.put("numberOfLayers", Integer.toString(numberOfLayers));
		resultsMap.put("numberOfNeurons", Integer.toString(numberOfNeurons));
		resultsMap.put("numberOfIterations", Integer.toString(numberOfIterations));
		
		return resultsMap;
	}
	
	// Method to create a parameter map to add mlp parameters
	private Map<String, String[]> getParameterMap(int numberOfLayers, int numberOfNeurons, int numberOfIterations) {
		
		Map<String, String[]> parameterMap = new HashMap<String, String[]>();
		
		String[] selection = {""};
		String[] layer1 = {""};
		String[] layer2 = {""};
		String[] layer3 = {""};
		String[] iterations = {""};
		
		switch (numberOfLayers) {
		
	        case 1:  	selection[0] = "mlp-01layer";
	        			layer1[0] = Integer.toString(numberOfNeurons * allocationFactor);
	        			iterations[0] = Integer.toString(numberOfIterations * allocationFactor);
	        			break;
	                 
	        case 2:  	selection[0] = "mlp-02layer";
	        			layer1[0] = Integer.toString(numberOfNeurons * allocationFactor);
	        			layer2[0] = Integer.toString(numberOfNeurons * allocationFactor);
	        			iterations[0] = Integer.toString(numberOfIterations * allocationFactor);
	                 	break;
	                 
	        case 3:  	selection[0] = "mlp-03layer";
	        			layer1[0] = Integer.toString(numberOfNeurons * allocationFactor);
	        			layer2[0] = Integer.toString(numberOfNeurons * allocationFactor);
	        			layer3[0] = Integer.toString(numberOfNeurons * allocationFactor);
	        			iterations[0] = Integer.toString(numberOfIterations * allocationFactor);
	                 	break;
		}
		
		// Add variables to parameters map
		
		parameterMap.put("selection", selection);
		parameterMap.put("mlp-01layer-layer1", layer1);
		parameterMap.put("mlp-01layer-iterations", iterations);
		
		parameterMap.put("mlp-02layer-layer1", layer1);
		parameterMap.put("mlp-02layer-layer2", layer2);
		parameterMap.put("mlp-02layer-iterations", iterations);
		
		parameterMap.put("mlp-03layer-layer1", layer1);
		parameterMap.put("mlp-03layer-layer2", layer2);
		parameterMap.put("mlp-03layer-layer3", layer3);
		parameterMap.put("mlp-03layer-iterations", iterations);
		
		if (selection[0].equals("mlp-01layer")) {
			
			System.out.println("MLP parameters: Layer1: " + layer1[0] + " Iterations: " + iterations[0]);
			
		} else if (selection[0].equals("mlp-02layer")) {
			
			System.out.println("MLP parameters: Layer1: " + layer1[0] + " Layer2: " + layer1[0] + " Iterations: " + iterations[0]);
			
		} else if (selection[0].equals("mlp-03layer")) {
			
			System.out.println("MLP parameters: Layer1: " + layer1[0] + " Layer2: " + layer1[0] + " Layer3: " + layer3[0] + " Iterations: " + iterations[0]);
		}
		
		return parameterMap;
	}
	
	// Method to get allocation factor
	public static int getAllocationFactor() {
		return allocationFactor;
	}

	// Calculate random integer with min and max
    private int randInt(int min, int max) {

        Random rand = new Random();
        int randomNum = rand.nextInt((max - min) + 1) + min;

        return randomNum;
    }
}
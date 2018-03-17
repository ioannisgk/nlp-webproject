package com.ioannisgk.nlpwebproject.controllers;

import java.io.IOException;
import edu.stanford.nlp.simple.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import com.ioannisgk.nlpwebproject.services.DatasetService;
import com.ioannisgk.nlpwebproject.services.ExecuteGA;
import com.ioannisgk.nlpwebproject.services.NeuralNetworkService;
import com.ioannisgk.nlpwebproject.services.ProcessResponse;
import com.ioannisgk.nlpwebproject.utils.Stemmer;

import edu.stanford.nlp.simple.Sentence;

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//                                                                                                                 //
// THIS IS THE MAIN CONTROLLER CLASS TO PROCESS DATA, SAVE DATA TO THE MODEL AND OPEN JSP VIEWS                    //
// CONTROLLER METHODS(3): CREATE, TRAIN AND TEST MLP NETWORK AND GET CATEGORY AND WORDS WITH NER TAGS FROM CORENLP //
// CREATE POPULATION, EVOLVE POPULATION, GET OPTIMAL NUMBER OF LAYERS, NEURONS AND ITERATIONS FOR MLP              //
// HELPER METHODS(2): CREATE MLP BASED ON USER SETTINGS AND UPDATE SESSION WITH NEW NER TAGS OF NEW MESSAGES       //
//                                                                                                                 //
// NOTE: This class processes data using the services classes. Stanford CoreNLP:                                   //
// LINK: https://github.com/stanfordnlp/CoreNLP (Used to get words, pos and ner tags from user message)            //
//                                                                                                                 //
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

@Controller
@RequestMapping("/home")
public class HomeController {
	
	private static final String WELCOME_MESSAGE = "<strong>Bot</strong>: Welcome, how can I help you?";
	private static final int numberOfGenerations = 5;
	
	// Inject the neural network service
	@Autowired
	private NeuralNetworkService neuralNetworkService;
	
	// Inject the dataset service
	@Autowired
	private DatasetService datasetService;
		
	// Inject the process response service
	@Autowired
	private ProcessResponse processResponse;
	
	// Inject the stemmer utility
	@Autowired
	private Stemmer stemmer;
	
	// Inject the execute GA service
	@Autowired
	private ExecuteGA executeGA;
	
	@GetMapping("/main")
	public String main(Model theModel) {
		
		// Open main.jsp
		return "main";
	}
	
	//////////////////////////////////////////////////////////////////////////////////////////////////////
	//                                                                                                  //
	// CONTROLLER METHODS(3): Create, train, save and test an MLP neural network, and show chat page,   //
	// get user message, selected category, test it with the MLP, get words with ner tags and response, //
	// create population, evolve population, get optimal number of layers, neurons and iterations.      //
	//                                                                                                  //
	//////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@PostMapping("/chat")
	public String chat(HttpServletRequest request, HttpSession session, Model theModel) {
		
		// Remove all session attributes when starting new chat
		
		if (session.getAttribute("person") != null) session.removeAttribute("person");
		if (session.getAttribute("location") != null) session.removeAttribute("location");
		if (session.getAttribute("date") != null) session.removeAttribute("date");
		if (session.getAttribute("duration") != null) session.removeAttribute("duration");
		if (session.getAttribute("money") != null) session.removeAttribute("money");
		
		// Get parameter map with user settings
		Map<String, String[]> parameterMap = request.getParameterMap();
		
		// Create an MLP neural network based on user settings
		MultiLayerPerceptron mlp = createMlp(parameterMap);
		
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
		
		// Add attributes to the model
		
    	theModel.addAttribute("message", WELCOME_MESSAGE);
    	theModel.addAttribute("overallProbability", resultsMap.get("overallProbability"));
    	theModel.addAttribute("correctPredictions", resultsMap.get("correctPredictions"));
    	
		// Open chat.jsp
		return "chat";
	}
	
	@PostMapping("/start-chat")
	@ResponseBody
	public String startChat(HttpServletRequest request, HttpSession session, Model theModel) {
		
		// Get parameter map with user settings
		Map<String, String[]> parameterMap = request.getParameterMap();
		
		// Get user input message from the parameter map
		String message = parameterMap.get("message")[0];
		
		// Load trained neural network from file
		NeuralNetwork nnet = NeuralNetwork.createFromFile("/opt/tomcat/tomcat-latest/mlp.nnet");
		
		// Test the neural network on the user message and get selected category and probability
		Map<String, String> resultsMap = neuralNetworkService.testNeuralNetwork(nnet, message);
		
		// Get selected category by the neural network
		String category = resultsMap.get("selectedCategory");
		
		// Remove "'" from user message because it causes javascript to crash
		message = message.replaceAll("'", "");
		
		// Create sentence object from user message
		Sentence theMessage = new Sentence(message);
		
		// Use Stanford CoreNLP library to get words, pos and ner tags from sentence
		
		List<String> theWords = theMessage.words();
		List<String> thePosTags = theMessage.posTags();
    	List<String> theNerTags = theMessage.nerTags();
    	
    	// Get a map with strings that contain related ner words from the user message
    	Map<String, String> theNerTagsMap = processResponse.getNerTagsMap(theWords, theNerTags);
    	
    	// Update the http session with new ner tags based on new user input message
    	session = updateHttpSession(session, theNerTagsMap);
    	
    	// Save each ner tag from the session to a string
    	
    	String person = (String) session.getAttribute("person");
    	String location = (String) session.getAttribute("location");
    	String date = (String) session.getAttribute("date");
    	String duration = (String) session.getAttribute("duration");
    	String money = (String) session.getAttribute("money");
    	
    	// Process ner tags and selected category from the user message and provide a response string
    	String response = processResponse.getResponse(person, location, date, duration, money, category);
    	
    	// Create array for holding strings to be used in javascript for generating info
    	String[][] theInfoArray = new String[theWords.size()][7];
    	
    	for (int i = 0; i < theWords.size(); i++) {
    		
    		// Add user message, words, pos and ner tags to the array
    		
    		theInfoArray[i][0] = "'" + message + "'";
    		theInfoArray[i][1] = "'" + theWords.get(i) + "'";
    		theInfoArray[i][2] = "'" + thePosTags.get(i) + "'";
    		theInfoArray[i][3] = "'" + theNerTags.get(i) + "'";
    		
    		// Add selected category, probability and response to the array
    		
    		theInfoArray[i][4] = "'" + resultsMap.get("selectedCategory") + "'";
    		theInfoArray[i][5] = "'" + resultsMap.get("probability") + "'";
    		theInfoArray[i][6] = "'" + response + "'";
    	}
    	
    	// Create a compatible string from the array, to be used in javascript
    	String theInfoForJs = Arrays.deepToString(theInfoArray);
    	
		return theInfoForJs;
	}
	
	@PostMapping("/execute-ga")
	@ResponseBody
	public String executeGA(Model theModel) {
		
		// Create a new population with individuals
		List<int[]> population = executeGA.createPopulation();
    	
    	int generationCount = 0;
    	int allocationFactor = executeGA.getAllocationFactor();
    	
    	for (int i = 0; i < numberOfGenerations; i++) {
    		
    		// Get the fittest individual from a population
    		int[] fittest = executeGA.getFittest(population);
        	
    		// Get the fitness of the fittest individual
        	double fitness = executeGA.getFitness(fittest);
        	
        	// Decode individual into mlp parameters
    		
    		Map<String, String> individualResultsMap = executeGA.getIndividualResultsMap(fittest);
    		
    		int numberOfLayers = Integer.parseInt(individualResultsMap.get("numberOfLayers"));
    		int numberOfNeurons = Integer.parseInt(individualResultsMap.get("numberOfNeurons"));
    		int numberOfIterations = Integer.parseInt(individualResultsMap.get("numberOfIterations"));
        	
    		System.out.println("\n- Generation: " + generationCount +
    										", Layers: " + numberOfLayers + 
    										", Neurons: " + numberOfNeurons * allocationFactor + 
    										", Iterations: " + numberOfIterations * allocationFactor + 
    										", Fitness: " + fitness + "%");
        	
    		// Evolve the population
        	population = executeGA.evolvePopulation(population);
        	
        	System.out.println("- Population evolved");
        	
        	generationCount++;
    	}
    	
    	// Get the fittest individual from current population
    	int[] fittest = executeGA.getFittest(population);
    	
    	// Get the fitness of the fittest individual
    	double fitness = executeGA.getFitness(fittest);
    	
    	// Decode individual into mlp parameters
		
		Map<String, String> individualResultsMap = executeGA.getIndividualResultsMap(fittest);
		
		int numberOfLayers = Integer.parseInt(individualResultsMap.get("numberOfLayers"));
		int numberOfNeurons = Integer.parseInt(individualResultsMap.get("numberOfNeurons"));
		int numberOfIterations = Integer.parseInt(individualResultsMap.get("numberOfIterations"));
		
		String result = "Layers: " + numberOfLayers + 
				", Neurons: " + numberOfNeurons * allocationFactor + 
				", Iterations: " + numberOfIterations * allocationFactor + 
				", Fitness: " + fitness + "%";
		
		System.out.println("\n" + result);
		
		return result;
	}
	
	/////////////////////////////////////////////////////////////////////////////////
	//                                                                             //
	// HELPER METHODS(2): Create an MLP neural network based on user settings,     //
	// update the http session with new ner tags based on new user input messages. //
	//                                                                             //
	/////////////////////////////////////////////////////////////////////////////////
	
	// Method to create an MLP neural network based on user settings
	public MultiLayerPerceptron createMlp(Map<String, String[]> parameterMap) {
		
		MultiLayerPerceptron mlp = null;
		
		// Get selected mode from the parameter map
		String selectedMode = parameterMap.get("selection")[0];
		
		if (selectedMode.equals("untrained")) {
			
			// If mode is untrained, set mode and iterations
			
			neuralNetworkService.setMode(selectedMode);
			neuralNetworkService.setIterations(0);
			
		} else if (selectedMode.equals("mlp-01layer")) {
			
			// Get layer1 size and iterations from parameter map
			
			String layer1Size = parameterMap.get("mlp-01layer-layer1")[0];
			String iterations = parameterMap.get("mlp-01layer-iterations")[0];
			
			// If mode is mlp-01layer, set mode, layer1 size and iterations
			
			neuralNetworkService.setMode(selectedMode);
			neuralNetworkService.setLayer1Size(Integer.parseInt(layer1Size));
			neuralNetworkService.setIterations(Integer.parseInt(iterations));
			
			
		} else if (selectedMode.equals("mlp-02layer")) {
			
			// Get layer1, layer2 sizes and iterations from parameter map
			
			String layer1Size = parameterMap.get("mlp-02layer-layer1")[0];
			String layer2Size = parameterMap.get("mlp-02layer-layer2")[0];
			String iterations = parameterMap.get("mlp-02layer-iterations")[0];
			
			// If mode is mlp-02layer, set mode, layer1, layer2 sizes and iterations
			
			neuralNetworkService.setMode(selectedMode);
			neuralNetworkService.setLayer1Size(Integer.parseInt(layer1Size));
			neuralNetworkService.setLayer2Size(Integer.parseInt(layer2Size));
			neuralNetworkService.setIterations(Integer.parseInt(iterations));
			
			
		} else if (selectedMode.equals("mlp-03layer")) {
			
			// Get layer1, layer2, layer3 sizes and iterations from parameter map
			
			String layer1Size = parameterMap.get("mlp-03layer-layer1")[0];
			String layer2Size = parameterMap.get("mlp-03layer-layer2")[0];
			String layer3Size = parameterMap.get("mlp-03layer-layer3")[0];
			String iterations = parameterMap.get("mlp-03layer-iterations")[0];
			
			// If mode is mlp-03layer, set mode, layer1, layer2, layer3 sizes and iterations
			
			neuralNetworkService.setMode(selectedMode);
			neuralNetworkService.setLayer1Size(Integer.parseInt(layer1Size));
			neuralNetworkService.setLayer2Size(Integer.parseInt(layer2Size));
			neuralNetworkService.setLayer3Size(Integer.parseInt(layer3Size));
			neuralNetworkService.setIterations(Integer.parseInt(iterations));
		}
		
		// Create the MLP neural network using the service
		mlp = neuralNetworkService.createMultiLayerPerceptron();
		
		return mlp;
	}
	
	// Update the http session with new ner tags based on new user input message
	private HttpSession updateHttpSession(HttpSession session, Map<String, String> theNerTagsMap) {
		
		if (session.getAttribute("person") == null) {
    		session.setAttribute("person", theNerTagsMap.get("person"));
    		
    	} else {
    		
    		String person = session.getAttribute("person") + " " + theNerTagsMap.get("person");
    		session.setAttribute("person", person.trim());
    	}
    	
		if (session.getAttribute("location") == null) {
    		session.setAttribute("location", theNerTagsMap.get("location"));
    		
    	} else {
    		
    		String location = session.getAttribute("location") + " " + theNerTagsMap.get("location");
    		session.setAttribute("location", location.trim());
    	}
		
		if (session.getAttribute("date") == null) {
    		session.setAttribute("date", theNerTagsMap.get("date"));
    		
    	} else {
    		
    		String date = session.getAttribute("date") + " " + theNerTagsMap.get("date");
    		session.setAttribute("date", date.trim());
    	}
		
		if (session.getAttribute("duration") == null) {
    		session.setAttribute("duration", theNerTagsMap.get("duration"));
    		
    	} else {
    		
    		String duration = session.getAttribute("duration") + " " + theNerTagsMap.get("duration");
    		session.setAttribute("duration", duration.trim());
    	}
		
		if (session.getAttribute("money") == null) {
    		session.setAttribute("money", theNerTagsMap.get("money"));
    		
    	} else {
    		
    		String money = session.getAttribute("money") + " " + theNerTagsMap.get("money");
    		session.setAttribute("money", money.trim());
    	}
		
		return session;
	}
}
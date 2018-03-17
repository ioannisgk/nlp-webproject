package com.ioannisgk.nlpwebproject.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProcessResponse {
	
	// Inject the dataset service
	@Autowired
	private DatasetService datasetService;
	
	//////////////////////////////////////////////////////////////////////////////////////////////////////////
	//                                                                                                      //
	// GET METHODS(2): Process ner tags and selected category from the user message and provide a response, //
	// get a map with strings that contain related ner words from the user message.                         //
	//                                                                                                      //
	//////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	// Method to process ner tags and selected category from the user message and provide a response string
	public String getResponse(String person, String location, String date, String duration, String money, String category) {
		
		// Get a map of all responses
		Map<String, String[]> responsesMap = datasetService.getAllResponsesMap();
		
		// Get array of strings that contain possible responses based on selected category from the neural network
		String[] possibleResponses = responsesMap.get(category);
		
		String response = "";
		
		// If a string with related ner tags is empty, generate a random response,
		// then ask the question from the missing set (directed chat strategy in order to fill "slots")
		
		if (person.isEmpty() || location.isEmpty() || date.isEmpty() || duration.isEmpty() || money.isEmpty()) {
			
			// Generate a random response from possible responses
			response = possibleResponses[randInt(0, possibleResponses.length-1)];
			
			if (person.isEmpty()) {
				
				// If there is no person information, ask the question from the missing set
				
				int index = randInt(0, responsesMap.get("personSet").length-1);
				response = response + " " + responsesMap.get("personSet")[index];
			}
	
			if (location.isEmpty()) {
				
				// If there is no location information, ask the question from the missing set
		
				int index = randInt(0, responsesMap.get("locationSet").length-1);
				response = response + " " + responsesMap.get("locationSet")[index];
			}
	
			if (date.isEmpty()) {
				
				// If there is no date information, ask the question from the missing set
				
				int index = randInt(0, responsesMap.get("dateSet").length-1);
				response = response + " " + responsesMap.get("dateSet")[index];
			}
	
			if (duration.isEmpty()) {
				
				// If there is no duration information, ask the question from the missing set
				
				int index = randInt(0, responsesMap.get("durationSet").length-1);
				response = response + " " + responsesMap.get("durationSet")[index];
			}
			
			if (money.isEmpty()) {
				
				// If there is no money information, ask the question from the missing set
				
				int index = randInt(0, responsesMap.get("moneySet").length-1);
				response = response + " " + responsesMap.get("moneySet")[index];
			}
		}
		
		// If all strings with related ner tags are filled, then
		// if the category equals to one of a specific set of categories (business rule),
		// create an ending message with all the information that the user has entered
		
		if (!person.isEmpty() && !location.isEmpty() && !date.isEmpty() && !duration.isEmpty() && !money.isEmpty()) {
			
			if (category.equals("General Travel Support") ||
					category.equals("Making a Plane Reservation") ||
					category.equals("Booking a Flight Online") ||
					category.equals("Buying a Plane Ticket") ||
					category.equals("Making a Hotel Reservation")) {
				
				// Create an ending message from the ending set
				
				int index = randInt(0, responsesMap.get("endingSet").length-1);
				response = response + " " + responsesMap.get("endingSet")[index];
				
				// Add to the response the information that the user has entered
				
				response = response + " Your details are: " + 
											" Name: " + person + 
											", Location: " + location + 
											", Date: " + date +
											", Duration: " + duration +
											", Budget: " + money + ".";
			} else {
				
				// Generate a random response from possible responses
				response = possibleResponses[randInt(0, possibleResponses.length-1)];
			}
		}
		
		return response;
	}
	
	// Method to get a map with strings that contain related ner words from the user message
	public Map<String, String> getNerTagsMap(List<String> theWords, List<String> theNerTags) {
		
		String person = "";
		String location = "";
		String date = "";
		String duration = "";
		String money = "";
		List<String> thePersonWords = new ArrayList<String>();
		List<String> theLocationWords = new ArrayList<String>();
		List<String> theDateWords = new ArrayList<String>();
		List<String> theDurationWords = new ArrayList<String>();
		List<String> theMoneyWords = new ArrayList<String>();
		Map<String, String> theNerTagsMap = new HashMap<>();
		
		// Iterate all words and add them to specific lists according to their ner tag
		
		for (int i = 0; i < theWords.size(); i++) {
			
			switch (theNerTags.get(i)) {
			
	            case "PERSON":  			thePersonWords.add(theWords.get(i));
	                     					break;
	                     			
	            case "LOCATION":  			theLocationWords.add(theWords.get(i));
	                     					break;
	                     			
	            case "COUNTRY":  			theLocationWords.add(theWords.get(i));
     										break;
     										
	            case "STATE_OR_PROVINCE":  	theLocationWords.add(theWords.get(i));
     										break;
     										
	            case "CITY":  				theLocationWords.add(theWords.get(i));
     										break;
	                     			
	            case "DATE":  				theDateWords.add(theWords.get(i));
     										break;
     								
	            case "DURATION":  			theDurationWords.add(theWords.get(i));
											break;
											
	            case "NUMBER":  			theDurationWords.add(theWords.get(i));
											break;
				
	            case "TIME":  				theDurationWords.add(theWords.get(i));
											break;
	                     			
	            case "MONEY":  				theMoneyWords.add(theWords.get(i));
	                     					break;
			}
		}
		
		// Create one string for each tag that contains the corresponding words
		
		for (String word: thePersonWords) person = person + " " + word;
		for (String word: theLocationWords) location = location + " " + word;
		for (String word: theDateWords) date = date + " " + word;
		for (String word: theDurationWords) duration = duration + " " + word;
		for (String word: theMoneyWords) money = money + " " + word;
		
		// Add strings that contain related ner words, to the map
		
		theNerTagsMap.put("person", person.trim());
		theNerTagsMap.put("location", location.trim());
		theNerTagsMap.put("date", date.trim());
		theNerTagsMap.put("duration", duration.trim());
		theNerTagsMap.put("money", money.trim());
		
		return theNerTagsMap;
	}
	
	// Calculate random integer with min and max
    private int randInt(int min, int max) {

        Random rand = new Random();
        int randomNum = rand.nextInt((max - min) + 1) + min;

        return randomNum;
    }
}
package com.ioannisgk.nlpwebproject.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ioannisgk.nlpwebproject.repositories.DatasetDAO;
import com.ioannisgk.nlpwebproject.utils.Stemmer;

////////////////////////////////////////////////////////////////////////////////////////////////////////////
//                                                                                                        //
// GET METHODS(4): GET NUMBER OF INPUT AND OUTPUT NEURONS, TEST SET AND TRAINING SET FOR NETWORK          //
// CREATE METHODS(4): CREATE BINARY LISTS OF CATEGORIES, SENTENCES SETS, TEST SETS, USER MESSAGE          //
// HELPER METHODS(4): COMPARE STEMMED SENTENCE TO CORPUS, CREATE CORPUS, STEM AND REMOVE DUPLICATES       //
//                                                                                                        //
// NOTE: This class accesses and processes data from the DatasetDAO class. Guide for text classification: //
// LINK: https://machinelearnings.co/text-classification-using-neural-networks-f5cd7b8765c6               //
//                                                                                                        //
////////////////////////////////////////////////////////////////////////////////////////////////////////////

@Service
public class DatasetService {
	
	// Inject the dataset dao
	@Autowired
	private DatasetDAO datasetDAO;
	
	// Inject the stemmer utility
	@Autowired
	private Stemmer stemmer;
	
	// Method to get a map of all responses
	public Map<String, String[]> getAllResponsesMap() {
		
		return datasetDAO.getAllResponsesMap();
	}
	
	// Method to get all categories as a list
	public List<String> getAllResultsList() {
		
		return datasetDAO.getAllResultsList();
	}
	
	///////////////////////////////////////////////////////////////////////////////////
	//                                                                               //
	// GET METHODS(4): Get number of input and output neurons on the neural network, //
	// get the test set and training set ready for use with the neural network.      //
	//                                                                               //
	///////////////////////////////////////////////////////////////////////////////////
	
	// Method to get number of input neurons on the neural network
	public int getInputNeurons() {
		
		// Create a list of training binary arrays based on training dataset
		List<double[]> datasetBinaryList = createTrainingDatasetBinaryList();
		
		// Input neurons equal the array length
		int inputNeurons = datasetBinaryList.get(0).length;
		
		return inputNeurons;
	}
	
	// Method to get number of output neurons on the neural network
	public int getOutputNeurons() {
		
		// Create a list of result binary arrays based on result dataset
		List<double[]> resultBinaryList = createResultDatasetBinaryList();
		
		// Output neurons equal the array length 
		int outputNeurons = resultBinaryList.get(0).length;
		
		return outputNeurons;
	}
	
	// Method to get the test dataset ready for use with the neural network
	public DataSet getTestDataset() {
		
		// Create lists of binary arrays based on test and result datasets
		
		List<double[]> datasetBinaryList = createTestDatasetBinaryList();
		List<double[]> resultBinaryList = createResultDatasetBinaryList();
		
		// Get number of input and output neurons
		
		int inputNeurons = datasetBinaryList.get(0).length;
		int outputNeurons = resultBinaryList.get(0).length;
		
		// Create dataset
		DataSet testSet = new DataSet(inputNeurons, outputNeurons);
		
		for (int i = 0; i < datasetBinaryList.size(); i++) {
			
			// Add test and result rows to the dataset
			
			double[] data = datasetBinaryList.get(i);
			double[] result = resultBinaryList.get(i);
			testSet.addRow(new DataSetRow(data, result));
		}
		
		return testSet;
	}
	
	// Method to get the training dataset ready for use with the neural network
	public DataSet getTrainingDataset() {
		
		// Create lists of binary arrays based on training and result datasets
		
		List<double[]> datasetBinaryList = createTrainingDatasetBinaryList();
		List<double[]> resultBinaryList = createResultDatasetBinaryList();
		
		// Get number of input and output neurons
		
		int inputNeurons = datasetBinaryList.get(0).length;
		int outputNeurons = resultBinaryList.get(0).length;
		
		// Create dataset
		DataSet trainingSet = new DataSet(inputNeurons, outputNeurons);
		
		for (int i = 0; i < datasetBinaryList.size(); i++) {
			
			// Add training and result rows to the dataset
			
			double[] data = datasetBinaryList.get(i);
			double[] result = resultBinaryList.get(i);
			trainingSet.addRow(new DataSetRow(data, result));
		}
		
		return trainingSet;
	}
	
	/////////////////////////////////////////////////////////////////////////////////////////////
	//                                                                                         //
	// CREATE METHODS(4): Create lists of binary arrays based on: result dataset (categories), //
	// training dataset (sentences sets), test dataset (test sets) and user message.           //
	//                                                                                         //
	/////////////////////////////////////////////////////////////////////////////////////////////
	
	// Method to create a list of result binary arrays based on result dataset
	public List<double[]> createResultDatasetBinaryList() {
		
		// Get all categories sentences as a string list
		
		List<double[]> resultBinaryList = new ArrayList<double[]>();
		List<String> datasetResultList = datasetDAO.getAllResultsList();
		int k = 0;
		
		// Iterate sentences list
		
		for (int i = 0; i < datasetResultList.size(); i++) {
			
			double[] results = new double[datasetResultList.size()];
			
			// If the category belongs to the current tenth group of results,
			// set current array element to 1
			
			for (int j = 0; j < datasetResultList.size(); j++) {
				
				results[j] = 0;
				if (j == k) results[j] = 1;				
			}
			k++;
			
			// Add the tenth group of results to the list
			for (int l = 0; l < 10; l++) resultBinaryList.add(results);
		}
		
		return resultBinaryList;
	}
	
	// Method to create a list of training binary arrays based on training dataset
	public List<double[]> createTrainingDatasetBinaryList() {
		
		List<double[]> datasetBinaryList = new ArrayList<double[]>();
		
		// Get all sentences sets as a string array list
		List<String[]> datasetSentencesList = datasetDAO.getAllSentencesList();
		
		// Create dataset corpus binary array
		String[] datasetCorpus = createDatasetCorpus();
		
		// Iterate sentences list
		
		for (int i = 0; i < datasetSentencesList.size(); i++) {
			
			String[] sentenceSet = datasetSentencesList.get(i);
			
			for (int j = 0; j < sentenceSet.length; j++) {
				
				// Stem each sentence and create stemmed sentence array
				
				String stemmedSentence = prepareStringData(sentenceSet[j]);
				String[] stemmedSentenceArray = stemmedSentence.split(" ");
				
				// Compare stemmed sentence to corpus and create binary array
				double[] sentenceFeatures = createSentenceFeatures(datasetCorpus, stemmedSentenceArray);
				
				// Add each binary array to the list
				datasetBinaryList.add(sentenceFeatures);
			}
		}
		
		return datasetBinaryList;
	}
	
	// Method to create a list of test binary arrays based on test dataset
	public List<double[]> createTestDatasetBinaryList() {
		
		List<double[]> datasetBinaryList = new ArrayList<double[]>();
		
		// Get all test sentences sets as a string array list
		List<String[]> datasetSentencesList = datasetDAO.getAllTestSentencesList();
		
		// Create dataset corpus binary array
		String[] datasetCorpus = createDatasetCorpus();
		
		// Iterate sentences list
		
		for (int i = 0; i < datasetSentencesList.size(); i++) {
			
			String[] sentenceSet = datasetSentencesList.get(i);
			
			for (int j = 0; j < sentenceSet.length; j++) {
				
				// Stem each sentence and create stemmed sentence array
				
				String stemmedSentence = prepareStringData(sentenceSet[j]);
				String[] stemmedSentenceArray = stemmedSentence.split(" ");
				
				// Compare stemmed sentence to corpus and create binary array
				double[] sentenceFeatures = createSentenceFeatures(datasetCorpus, stemmedSentenceArray);
				
				// Add each binary array to the list
				datasetBinaryList.add(sentenceFeatures);
			}
		}
		
		return datasetBinaryList;
	}
	
	// Method to create custom binary array based on user message
	public double[] createCustomDatasetBinary(String message) {
		
		// Stem the user message and create stemmed sentence array
		
		String stemmedSentence = prepareStringData(message) + " ";		
		String[] stemmedSentenceArray = stemmedSentence.split(" ");
		
		// Create dataset corpus binary array
		String[] datasetCorpus = createDatasetCorpus();
		
		// Compare stemmed sentence to corpus and create binary array
		double[] sentenceFeatures = createSentenceFeatures(datasetCorpus, stemmedSentenceArray);
		
		return sentenceFeatures;
	}
	
	////////////////////////////////////////////////////////////////////////////////////
	//                                                                                //
	// HELPER METHODS(4): Compare stemmed sentence to corpus and create binary array, //
	// create dataset corpus array, remove duplicates and stem sentences.             //
	//                                                                                //
	////////////////////////////////////////////////////////////////////////////////////
	
	// Method to compare stemmed sentence to corpus and create binary array
	private double[] createSentenceFeatures(String[] datasetCorpus, String[] stemmedSentenceArray) {
		
		double[] sentenceFeatures = new double[datasetCorpus.length];
		
		// Iterate corpus array and if the stemmed word on the sentence
		// is included in the corpus, set current array element to 1
		
		for (int k = 0; k < datasetCorpus.length; k++) {
			
			sentenceFeatures[k] = 0;
			
			for (int l = 0; l < stemmedSentenceArray.length; l++) {
				
				if (datasetCorpus[k].equals(stemmedSentenceArray[l])) {
					
					sentenceFeatures[k] = 1;
				}
			}
		}
		
		return sentenceFeatures;
	}
	
	// Method to create dataset corpus array, from all sentences sets
	private String[] createDatasetCorpus() {
		
		String stemmedSentence = "";
		String[] datasetCorpus = null;
		List<String[]> datasetSentencesList = datasetDAO.getAllSentencesList();
		
		// Iterate sentences list, remove duplicates and stem each sentence
		
		for (int i = 0; i < datasetSentencesList.size(); i++) {
			
			String[] sentenceSet = datasetSentencesList.get(i);
			
			for (int j = 0; j < sentenceSet.length; j++) {
				
				// Concatenate all stemmed sentences
				stemmedSentence = stemmedSentence + prepareStringData(sentenceSet[j]) + " ";
			}
		}
		
		// Remove duplicates and create dataset corpus array 
		
		stemmedSentence = removeDuplicateWords(stemmedSentence);
		datasetCorpus = (stemmedSentence.trim()).split(" ");
		
		return datasetCorpus;
	}
	
	// Method to convert to lowercase, remove duplicates and stem string
	private String prepareStringData(String data) {
		
		data = (data).toLowerCase();
		data = removeDuplicateWords(data);
		
		// Stem string using Stanford CoreNLP stemmer
		data = stemmer.stem(data);
		
		return data.trim();
	}
	
	// Method to remove duplicate words from a string
	private String removeDuplicateWords(String sentence) {
		
		String retVal = "";
		String[] words = sentence.split(" ");
		
		// Iterate array and remove duplicates
		
		for (int i = 0; i < words.length; i++) {
			if (words[i] != null) {
				for (int j = i + 1; j < words.length; j++) {
					if (words[i].equals(words[j])) words[j] = null;
				}
			}
		}
		
		// Recreate string
		
		for (int k = 0; k < words.length; k++) {
			if (words[k] != null) retVal = retVal + " " + words[k];
		}
		
		return retVal.trim();
	}
}
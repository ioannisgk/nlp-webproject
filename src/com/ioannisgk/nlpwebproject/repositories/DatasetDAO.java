package com.ioannisgk.nlpwebproject.repositories;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ioannisgk.nlpwebproject.utils.Stemmer;

////////////////////////////////////////////////////////////////////////////////////////////////
//                                                                                            //
// THIS CLASS CONTAINS HARDCODED DATA FOR TRAINING AND TESTING OF THE NEURAL NETWORK          //
// AND DIALOGUE RESPONSES ACCORDING TO THE CATEGORIES ASSIGNED BY THE NETWORK                 //
//                                                                                            //
// NOTE: We use hardcoded data, as this web application emphasizes on the use of NLP and ANN. //
// In a real environment, all data should have been parsed and saved to a database first.     //
//                                                                                            //
// LINK: https://www.eslfast.com/robot/topics/travel/travel01.htm (Conversations dataset)     //
//                                                                                            //
////////////////////////////////////////////////////////////////////////////////////////////////

@Repository
public class DatasetDAO {
	
	/////////////////////////////////////////////////////////////////////////////////
	//                                                                             //
	// CATEGORIES: There are 10 categories that the user input can be assigned to. //
	// SENTENCE SETS: Each sentence set is mapped to one and only one category.    //
	//                                                                             //
	/////////////////////////////////////////////////////////////////////////////////
	
	private String category01 = "General Travel Support";
	private String category02 = "Making a Plane Reservation";
	private String category03 = "Booking a Flight Online";
	private String category04 = "Buying a Plane Ticket";
	private String category05 = "Making a Hotel Reservation";
	private String category06 = "Missing Connecting Flight";
	private String category07 = "Ground Transportation";
	private String category08 = "Arranging a Tour of the City";
	private String category09 = "Complaining about a Tour";
	private String category10 = "Sightseeing";
	
	private String[] sentenceSet01 = {"I need help planning my vacation", 
			"I haven't decided where to go yet", 
			"I am thinking that I might enjoy a tropical climate", 
			"These look great", 
			"I have about a thousand dollars to spend on this trip", 
			"Could you help me with vacation plans", 
			"I am open to suggestions at this point", 
			"I would like to travel to a cooler destination", 
			"I will look at them right now", 
			"I think that I can spend about two hundred dollars a day"};
	
	private String[] sentenceSet02 = {"Could you help me make a plane reservation", 
			"I am going to go to Hawaii", 
			"I think that I would rather leave from Los Angeles Airport", 
			"I need to leave in the morning", 
			"I would prefer to return in the afternoon",
			"I need to make a plane reservation", 
			"I will be traveling to Aspen Colorado", 
			"Burbank Airport would be best for me", 
			"I can only take a flight that leaves in the afternoon", 
			"I think that I would like a morning return flight better"};
	
	private String[] sentenceSet03 = {"I have never booked a flight online", 
			"How do you book airline tickets online", 
			"What information do I need to provide to book a flight", 
			"How do I get the cheapest rate", 
			"Can I fly on different airlines", 
			"Do you think it is a good idea to book airline tickets online", 
			"Can you help me figure out how to book airline tickets online", 
			"What do I need to know to book a flight", 
			"How do I get the best price", 
			"Can I choose which airline I fly with"};
	
	private String[] sentenceSet04 = {"I would like to book a flight", 
			"I am traveling to Singapore", 
			"I want to fly on June 14th", 
			"I would like to fly out of Los Angeles International Airport",
			"I would rather fly in the morning",
			"I want to buy a plane ticket",
			"My final destination is Sydney Australia",
			"I am making a reservation for December 12th", 
			"I want to fly out of Burbank Airport",
			"I need an afternoon flight"};
	
	private String[] sentenceSet05 = {"I would like to make a hotel reservation", 
			"I will be arriving on May 14th", 
			"I need the room for 3 nights", 
			"I will be staying in the room alone", 
			"We need a nonsmoking room", 
			"I would like to book a reservation at your hotel", 
			"I need the reservation for May 14th", 
			"I will be staying for 3 nights", 
			"I need a double room", 
			"We require a smoking room"};
	
	private String[] sentenceSet06 = {"My flight just arrived late and I need to know what to do", 
			"Do I have to call anyone",
			"What if there are no more connecting flights for today on this airline", 
			"What if I can't find another flight out of here tonight", 
			"Will the airline pay for my room if I have to spend the night at the airport", 
			"Our flight was late and I missed my connecting flight", 
			"Whom should I call", 
			"I think the flight we missed was the last flight out of here today", 
			"What if I get stuck here and can't get out of this airport", 
			"I don't have any money for a hotel room"};
	
	private String[] sentenceSet07 = {"I just arrived and need help getting transportation to my hotel", 
			"Which one is the best form of transportation", 
			"Where can I catch a cab", 
			"Where can I rent a car", 
			"Are there any subways in this town", 
			"I need help figuring out how to get to my friend's house in the city", 
			"Which form of transportation is preferable", 
			"Where do the buses pick you up", 
			"Is there a car rental place around here", 
			"Is there a subway in this city"};
	
	private String[] sentenceSet08 = {"I was wondering if you could help me book a few tours", 
			"I will be here for a week", 
			"This is my first time visiting this city", 
			"I really enjoy visiting museums and art galleries", 
			"Do you have a city tour", 
			"I need help knowing how to book some tours", 
			"I am only staying for three days", 
			"I have been here before on a business trip but didn't really have a chance to see much", 
			"I love being outdoors and would love to spend some time at the beach", 
			"Do you have a tour where I can get a quick overview of the city"};
	
	private String[] sentenceSet09 = {"I am having some big problems on this tour", 
			"This tour company seems very disorganized No one seems to have a clear picture as to where we are going and when we are going to get there", 
			"So far we have been staying in really out of the way accommodations", 
			"The television in our room was broken and they didn't send anyone to fix it", 
			"The food in the restaurant was terrible and a few people from our group got sick", 
			"I paid good money for this tour and would appreciate some help with some problems that have come up", 
			"This tour company can't seem to coordinate anything We are always arriving after something is closed or get there when the guides for the site are on a break", 
			"Our accommodations are pretty substandard", 
			"The room smelled of smoke even though it was supposed to be a nonsmoking room", 
			"Actually a few people from our group got sick and everyone thinks it was from the bad dinner that we ate"};
	
	private String[] sentenceSet10 = {"Where should we go sightseeing today", 
			"I think that I would like to go to the beach this morning", 
			"I hear that there is a very nice natural history museum there", 
			"Where should we go in the afternoon", 
			"At the end of the day I would love to take in the sunset at the restaurant by the park", 
			"It's so hard to choose where to go first on our sightseeing trip", 
			"I really wanted to make sure that I got a chance to go to the local beach", 
			"I heard that the Natural History Museum is pretty close to the beach",
			"What would be a good place to go to in the afternoon", 
			"The restaurant on the edge of the park would be a great place to watch the sun go down"};
	
	/////////////////////////////////////////////////////////////////////////////////////
	//                                                                                 //
	// TEST SETS: Each test set is mapped to one and only one category.                //
	// NOTE: All test sets are 30% of the original dataset for more objective results. //
	//                                                                                 //
	/////////////////////////////////////////////////////////////////////////////////////
	
	private String[] testSet01 = {"I was wondering if you would be able to help me with vacation plans", 
			"I don't know where I want to go yet", 
			"A nice temperate climate would be best for me", 
			"This place looks nice", 
			"I just got a bonus and can spend about three thousand dollars total"};
			
	private String[] testSet02 = {"I am planning a trip and need help making my airline reservation", 
			"I will be vacationing in Oregon", 
			"I live closer to Los Angeles Airport so that would be the best choice", 
			"I can leave at either time", 
			"Either morning or afternoon would work for me"};
			
	private String[] testSet03 = {"Is booking airline tickets online a good way to go", 
			"How do you book airline tickets online",
			"What are they going to ask me to book a flight", 
			"Is there anything special I can do to get the lowest price", 
			"Do you have to fly with a particular airline"};
			
	private String[] testSet04 = {"I need to make a plane reservation", 
			"I need to fly to New York City", 
			"I need a flight on July 4th", 
			"I will fly out of whatever airport has the cheapest price", 
			"I would like to be booked on the least expensive flight"};
	
	private String[] testSet05 = {"I need to book a room at your hotel", 
			"Our stay will be beginning on May 14th", 
			"Please book the room for 3 nights", 
			"I need a room for 3 people",
			"We must have a nonsmoking room"};
	
	private String[] testSet06 = {"I had a connecting flight but our arriving flight was late", 
			"Can you give me the number of the airline so that I can call", 
			"I remember that when we booked this flight our connecting flight was the only one on this day", 
			"Where will I stay if I can't get another flight tonight", 
			"I don't think I should have to pay for a hotel room"};
			
	private String[] testSet07 = {"I need information about my options for ground transportation", 
			"Is one form of transportation better than another", 
			"Where are the shuttle pick up points", 
			"Where are the car rental agencies", 
			"I heard that you have a very good light rail system in this city"};
	
	private String[] testSet08 = {"I need help figuring out what to see and visit in this city", 
			"I am on a layover and will only have today and tomorrow morning to sightsee", 
			"Actually I was born here but haven't been here in twenty years", 
			"I was interested in checking out local architecture", 
			"Is there any way that I can take a quick tour of the city so that I can get some idea as to what I might like to see"};
	
	private String[] testSet09 = {"I would like to discuss some problems that I am having with this tour", 
			"This tour company doesn't seem to know the town very well We are always getting lost",
			"Our accommodations are not very pleasant", 
			"The room was very noisy and we couldn't get to sleep", 
			"The food was horrible at the hotel restaurant and a few of us got sick after eating there"};
	
	private String[] testSet10 = {"There are so many places to go on our sightseeing trip that I am having trouble narrowing it down",
			"I heard that the local beach is a place that can't be missed",
			"The Natural History Museum is close by isn't it",
			"Do you have any suggestions as to where we could go in the afternoon",
			"We could eat dinner and watch the sun go down at that restaurant by the park"};
	
	////////////////////////////////////////////////////////////////////////////////////////////
	//                                                                                        //
	// RESPONSE SETS: Each response set is mapped to one and only one category.               //
	// NOTE: The system selects randomly a response from a category assigned during training. //
	//                                                                                        //
	////////////////////////////////////////////////////////////////////////////////////////////
	
	private String[] responseSet01 = {"Do you enjoy warm or cold climates?", 
			"Well, I will send you these brochures, and get back to me when you want to make your reservations.", 
			"Do you want to travel to a tropical climate, or would you like to go somewhere with a cooler climate?",
			"I can send you some brochures that could give you some ideas.",
			"Take your time choosing a destination and, when you have narrowed it down, I will be happy to help you make a reservation." ,
			"Do you enjoy warm weather, or are you looking forward to a cooler vacation?",
			"Why dont you take a look at these brochures that might help you make up your mind?", 
			"I will be happy to help you make a reservation whenever you decide upon a destination."};
	
	private String[] responseSet02 = {"Would you prefer a morning or afternoon departure?",
			"Fine. On your return flight, do you have a preference as to morning or afternoon?",
			"I can book you on your flight but with a high rate. Would you accept that?", 
			"You can leave in the morning of afternoon from that airport. Do you have a preference?", 
			"I can book that for you right now. Would you prefer morning or afternoon?", 
			"You have an option of departing from two different airports. What is your preference?", 
			"Does it matter to you if you leave in the morning or afternoon?", 
			"I will print your tickets as soon as you book your flight."};
	
	private String[] responseSet03 = {"I have been booking airline tickets for customers many times. It has worked out great for them!", 
			"Usually you get the best price by having some flexibility in your travel time and dates.", 
			"I think that booking airline tickets online is the only way to go.", 
			"You will need to type in the dates that you wish to travel and where you are going.", 
			"If you have some flexibility in when you can travel, that will usually get you a better rate.", 
			"These sites deal with a lot of different airlines, so you can choose whichever one you prefer.",
			"I have been really good in booking airline tickets online.", 
			"The travel sites deal with many different airlines. It is always up to you to choose who you will fly with."};
	
	private String[] responseSet04 = {"Would you prefer a morning or an afternoon flight?", 
			"Well, I will book you on a flight that will fit your schedule.", 
			"Would you rather fly in the morning or later in the day?",
			"We can book your flight right now.", 
			"From what I see, you can fly out within the next two months.", 
			"If you have a choice, what time of day would you prefer to fly?", 
			"If you book your flight with us, your tickets will arrive in the mail within 3 days.", 
			"Would you prefer booking a business class or not?"};
	
	private String[] responseSet05 = {"How many people will be staying in the room?", 
			"Would you like a smoking or nonsmoking room?", 
			"Is that a single room, or will there be more guests?",
			"We have smoking and nonsmoking rooms. Which do you prefer?", 
			"Your room will be booked, we need a few more information.", 
			"Would you like a single or double room?", 
			"Do you need a nonsmoking room?", 
			"Would you like to reserve your room?"};
	
	private String[] responseSet06 = {"Thats okay. The airline computer is keeping track of what is happening with you.", 
			"By speaking with us at the arrival gate, we can fix the problem for you and direct you.", 
			"We can help you find transportation to a local hotel.", 
			"Since the flight delay was our fault and you are continuing on with our airline, we will cover the cost of a hotel room.", 
			"We can help you find an alternate flight that will get you where you are going.", 
			"We can go on the computer to try to find an alternate flight on another airline if we cannot accommodate you.", 
			"There is a shuttle that is still running and can take you into town to a hotel.",
			"We will help you make alternative plans."};
	
	private String[] responseSet07 = {"There are shuttles, taxis, and buses that go all over the city.", 
			"It depends on where you want to go. If you are going to a well-known hotel, they have their own shuttles that drop you right off at the door.", 
			"You may take a shuttle, a taxi, or a bus to get wherever you need to go.", 
			"It depends on what you are looking for. If you are looking to travel inexpensively, a bus might be your best bet.",
			"The ground transportation center is right outside the exit leading to the street.", 
			"Our subway station is a mile outside of the airport. I would suggest you take the designated shuttle to get there.", 
			"This city has many taxis, shuttles, or buses to transport you to your destination.", 
			"It depends on where you are traveling. If your destination is close by, you might want to just grab a taxicab."};
	
	private String[] responseSet08 = {"Have you ever visited our city before?", 
			"What are you interested in? Do you enjoy museums and buildings, or would you rather hit some outdoor hotspots and venues?", 
			"We have tours for all interests.", 
			"Is this your first stay in our city?", 
			"Have you thought of what types of things you would like to see? Would you maybe like to visit some outdoor venues or nighttime hot-spots?", 
			"Do you have any ideas of what you would like to see? Perhaps you would enjoy our world-renowned museums or a visit to our magnificent zoological gardens.", 
			"I think that you would enjoy a number of our tours.", 
			"A general city tour is an excellent tour to start with."};
	
	private String[] responseSet09 = {"I will see what I can do about that. How about the accommodations on the tour?",
			"I am so sorry that you had such a bad experience. We would like to offer you a free city tour and lunch to make it up to you.", 
			"We are trying to fix that. Are the accommodations to your liking?", 
			"You should not have to put up with that. Was the hotel restaurant good?", 
			"I am sorry you are having a difficult time. What may I help you with?", 
			"I am sorry that that has been the case. How would you rate your accommodations?", 
			"That is unacceptable. Did you at least enjoy your dinner in the hotel restaurant?",
			"I am glad that you told me about the problems. I would like you to be my guest for a free massage at the hotel spa tonight."};
	
	private String[] responseSet10 = {"I think that some things might be best done in the morning and others in the afternoon.", 
			"I think that I would like to go to the amusement park. It is supposed to be quite good.",
			"We could think of what would make a good morning activity versus an evening activity.",
			"The local amusement park is supposed to be wonderful.", 
			"Lets figure out what to do before lunch and later figure out what to do in the afternoon.", 
			"That would be a relaxing way to begin our morning. It would be good to get out in the sun.",
			"That museum is supposed to be fantastic!", 
			"We could check out the local amusement park."};
	
	//////////////////////////////////////////////////////////////////////////////////////////////////
	//                                                                                              //
	// DIALOGUE SETS: Each dialogue set is used to direct the conversation and ask for information. //
	// NOTE: Directed conversation is triggered only on specific user input categories.             //
	//                                                                                              //
	//////////////////////////////////////////////////////////////////////////////////////////////////
	
	private String[] personSet = {"Can you tell me your name for booking your trip?",
			"What is your name and surname?",
			"We need your full name to book this trip."};

	private String[] locationSet = {"Do you know where you will be traveling?",
			"Have you chosen your destination?",
			"Where do you plan on going?"};
	
	private String[] dateSet = {"What date will you be traveling?", 
			"What is your travel date?",
			"What date would you like to make that reservation for?"};
	
	private String[] durationSet = {"How long will you be staying?", 
			"How many days do you need the reservation for?", 
			"Can you tell me how many days you need the room for?"};
	
	private String[] moneySet = {"Do you know how much you want to spend on this vacation?", 
			"How much money is in your budget for this trip?", 
			"Have you thought about what you would like to spend on this vacation?"};

	private String[] endingSet = {"We have booked your flight and room.", 
			"I have you booked on a flight. Your tickets will arrive in the mail within 3 days.", 
			"I was able to book your flight, and I will print your tickets right now. Have a great trip!"};
	
	// Method to get all sentences as a list
	public List<String[]> getAllSentencesList() {
		
		List<String[]> sentencesList = new ArrayList<String[]>();
		
		sentencesList.add(sentenceSet01);
		sentencesList.add(sentenceSet02);
		sentencesList.add(sentenceSet03);
		sentencesList.add(sentenceSet04);
		sentencesList.add(sentenceSet05);
		sentencesList.add(sentenceSet06);
		sentencesList.add(sentenceSet07);
		sentencesList.add(sentenceSet08);
		sentencesList.add(sentenceSet09);
		sentencesList.add(sentenceSet10);
		
		return sentencesList;
	}
	
	// Method to get all test sentences as a list
	public List<String[]> getAllTestSentencesList() {
		
		List<String[]> sentencesList = new ArrayList<String[]>();
		
		sentencesList.add(testSet01);
		sentencesList.add(testSet02);
		sentencesList.add(testSet03);
		sentencesList.add(testSet04);
		sentencesList.add(testSet05);
		sentencesList.add(testSet06);
		sentencesList.add(testSet07);
		sentencesList.add(testSet08);
		sentencesList.add(testSet09);
		sentencesList.add(testSet10);
		
		return sentencesList;
	}
	
	// Method to get all categories as a list
	public List<String> getAllResultsList() {
		
		List<String> resultsList = new ArrayList<String>();
		
		resultsList.add(category01);
		resultsList.add(category02);
		resultsList.add(category03);
		resultsList.add(category04);
		resultsList.add(category05);
		resultsList.add(category06);
		resultsList.add(category07);
		resultsList.add(category08);
		resultsList.add(category09);
		resultsList.add(category10);
		
		return resultsList;
	}
	
	// Method to get all responses as a map
	public Map<String, String[]> getAllResponsesMap() {
		
		Map<String, String[]> responsesMap = new HashMap<>();
		
		responsesMap.put(category01, responseSet01);
		responsesMap.put(category02, responseSet02);
		responsesMap.put(category03, responseSet03);
		responsesMap.put(category04, responseSet04);
		responsesMap.put(category05, responseSet05);
		responsesMap.put(category06, responseSet06);
		responsesMap.put(category07, responseSet07);
		responsesMap.put(category08, responseSet08);
		responsesMap.put(category09, responseSet09);
		responsesMap.put(category10, responseSet10);
		
		responsesMap.put("personSet", personSet);
		responsesMap.put("locationSet", locationSet);
		responsesMap.put("dateSet", dateSet);
		responsesMap.put("durationSet", durationSet);
		responsesMap.put("moneySet", moneySet);
		responsesMap.put("endingSet", endingSet);
		
		return responsesMap;
	}
}
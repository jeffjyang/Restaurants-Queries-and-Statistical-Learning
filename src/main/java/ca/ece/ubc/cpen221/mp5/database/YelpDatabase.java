package ca.ece.ubc.cpen221.mp5.database;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.function.ToDoubleBiFunction;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;

import ca.ece.ubc.cpen221.mp5.MP5Db;

public class YelpDatabase implements MP5Db<Review>{
	private Set<YelpRestaurant> restaurants;
	private Set<User> users;
	private Set<Review> reviews;
	
	public YelpDatabase(String restaurantFileName, String reviewFileName, String userFileName) {
		this.restaurants = Collections.synchronizedSet(new HashSet<>());
		this.users = Collections.synchronizedSet(new HashSet<>());
		this.reviews = Collections.synchronizedSet(new HashSet<>());
		
		parseJsonRestaurant(restaurantFileName);
		parseJsonUsers(userFileName);
		parseJsonReviews(reviewFileName);
		
	}
	

	@Override
	public Set<Review> getMatches(String queryString) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String kMeansClusters_json(int k) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ToDoubleBiFunction<MP5Db<Review>, String> getPredictorFunction(String user) {
		// TODO Auto-generated method stub
		return null;
	}
	
	private void parseJsonRestaurant(String jsonDir) {
		InputStream is;
		
		try {
			is = new FileInputStream(new File(getClass().getResource(jsonDir).getFile()));
			JsonReader reader = Json.createReader(is);
			JsonArray array = reader.readArray();
			
			for (int index = 0; index < array.size(); index++) {
				JsonObject obj = array.getJsonObject(index);
				YelpRestaurant restaurant = new YelpRestaurant(obj);	
				this.restaurants.add(restaurant);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	private void parseJsonUsers(String jsonDir) {
		InputStream is;
		
		try {
			is = new FileInputStream(new File(getClass().getResource(jsonDir).getFile()));
			JsonReader reader = Json.createReader(is);
			JsonArray array = reader.readArray();
			
			for (int index = 0; index < array.size(); index++) {
				JsonObject obj = array.getJsonObject(index);
				YelpUser user = new YelpUser(obj);
				this.users.add(user);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	private void parseJsonReviews(String jsonDir) {
		InputStream is;
		
		try {
			is = new FileInputStream(new File(getClass().getResource(jsonDir).getFile()));
			JsonReader reader = Json.createReader(is);
			JsonArray array = reader.readArray();
			
			for (int index = 0; index < array.size(); index++) {
				JsonObject obj = array.getJsonObject(index);
				YelpReview review = new YelpReview(obj);
				this.reviews.add(review);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

}

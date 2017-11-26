package ca.ece.ubc.cpen221.mp5.database;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.function.ToDoubleBiFunction;

import ca.ece.ubc.cpen221.mp5.MP5Db;

public class YelpDatabase implements MP5Db<Review>{
	private Set<YelpRestaurant> restaurants;
	private Set<User> users;
	private Set<Review> reviews;
	
	public YelpDatabase(String restaurantFileName, String reviewFileName, String userFileName) {
		this.restaurants = Collections.synchronizedSet(new HashSet<>());
		this.users = Collections.synchronizedSet(new HashSet<>());
		this.reviews = Collections.synchronizedSet(new HashSet<>());
		
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
	
	private void parseJson() {
		
	}

}

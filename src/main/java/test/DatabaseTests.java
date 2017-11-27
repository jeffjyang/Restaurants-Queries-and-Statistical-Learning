package test;

import java.util.Set;



import org.junit.Test;

import ca.ece.ubc.cpen221.mp5.database.YelpDatabase;
import ca.ece.ubc.cpen221.mp5.database.YelpRestaurant;
public class DatabaseTests {
	String restaurantJSON = "data/restaurants.JSON";
	String reviewJSON = "data/reviews.json";
	String userJSON = "data/users.json";
	
	Set<YelpRestaurant> restaurants;
	@Test
    public void test00() {
		YelpDatabase database = new YelpDatabase(restaurantJSON, reviewJSON, userJSON);
		Set<YelpRestaurant> restaurants = database.getRestaurants();
		
		for (YelpRestaurant rest : restaurants) {
			System.out.println(rest.getFullAddress());
		}
		
		}
}



package test;

import java.util.Set;

import ca.ece.ubc.cpen221.mp5.database.YelpDatabase;
import ca.ece.ubc.cpen221.mp5.database.YelpRestaurant;
import org.junit.Test;

public class DatabaseTests {
	private String restaurantJSON = "data/restaurants.JSON";
	private String reviewJSON = "data/reviews.json";
	private String userJSON = "data/users.json";
	
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



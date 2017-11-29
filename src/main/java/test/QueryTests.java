package test;

import org.junit.Test;

import ca.ece.ubc.cpen221.mp5.database.YelpDatabase;
import ca.ece.ubc.cpen221.mp5.query.Query;

public class QueryTests {
	private final String restaurantJSON = "data/restaurants.JSON";
	private final String reviewJSON = "data/reviews.json";
	private final String userJSON = "data/users.json";
	
	@Test
    public void Test00 () {
		YelpDatabase database = new YelpDatabase(restaurantJSON, reviewJSON, userJSON);
		String query = "(category(Chinese)";
		Query queryObj = new Query();
		
		queryObj.queryDatabase(database, query);
	}
}

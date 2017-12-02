package ca.ubc.ece.cpen221.mp5.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Map;

import org.junit.Test;

import ca.ece.ubc.cpen221.mp5.database.YelpDatabase;
import ca.ece.ubc.cpen221.mp5.database.YelpUser;

public class UserTest {
	private String restaurantJSON = "data/restaurants.json";
	private String reviewJSON = "data/reviews.json";
	private String userJSON = "data/users.json";

	@Test
	public void testUser1() { 
		YelpDatabase database = new YelpDatabase(restaurantJSON, reviewJSON, userJSON);
		YelpUser user = database.getUser("_NH7Cpq3qZkByP5xR4gXog");
		
		user.addReview("REVIEW");
		assertTrue(user.getReviewCount() == 30);
		List<String> userReviews = user.getReviews();
		user.removeReview("REVIEW");
		user.removeReview("NOTTHERE");
		assertTrue(user.getReviewCount() == 29);
		assertTrue(user.getAverageStars() < 4);
		assertEquals("user", user.getType());
		assertEquals("http://www.yelp.com/user_details?userid=_NH7Cpq3qZkByP5xR4gXog", user.getUrl());
		assertEquals("Chris M.", user.getName());
		user.setName("NotChris");
		assertEquals("NotChris", user.getName());
		Map<String, Integer> votes = user.getVotes();
		
	}

}

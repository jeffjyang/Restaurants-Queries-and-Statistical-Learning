package ca.ubc.ece.cpen221.mp5.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

import ca.ece.ubc.cpen221.mp5.database.YelpDatabase;
import ca.ece.ubc.cpen221.mp5.database.YelpRestaurant;

public class RestaurantTests {
	private String restaurantJSON = "data/restaurants.json";
	private String reviewJSON = "data/reviews.json";
	private String userJSON = "data/users.json";
	
	@Test
	public void testRestaurant1() { 
		YelpDatabase database = new YelpDatabase(restaurantJSON, reviewJSON, userJSON);
		YelpRestaurant restaurant = database.getRestaurant("gclB3ED6uk6viWlolSb_uA");
		assertTrue(restaurant.getReviewCount() == 9);
		restaurant.addReview("newreview");
		assertTrue(restaurant.getReviewCount() == 10);
		restaurant.removeReview("newreview");
		restaurant.removeReview("notreview");
		List<String> reviews = restaurant.getReviews();
		restaurant.setName("New Name");
		assertEquals("New Name", restaurant.getName());
		assertEquals("CA", restaurant.getState());
		assertEquals("Berkeley", restaurant.getCity());
		assertEquals("2400 Durant Ave\nTelegraph Ave\nBerkeley, CA 94701", restaurant.getFullAddress());
		restaurant.setUrl("google.com");
		assertEquals("google.com", restaurant.getUrl());
		restaurant.removeCategory("Cafes");
		restaurant.removeCategory("not category");
		restaurant.addCategory("Chinese");
		restaurant.setStars(2.3);
		restaurant.setPrice(3);
		assertTrue(restaurant.isOpen());
		restaurant.getPhotoUrl();
		restaurant.getSchools();
		restaurant.getType();
		restaurant.addReview();
		restaurant.removeReview();
		restaurant.setPhotoUrl("google.com");
		YelpRestaurant restaurantSame = database.getRestaurant("gclB3ED6uk6viWlolSb_uA");
		assertEquals(restaurant, restaurantSame);
		Object obj = new Object();
		assertFalse(restaurant.equals(obj));

	}
}

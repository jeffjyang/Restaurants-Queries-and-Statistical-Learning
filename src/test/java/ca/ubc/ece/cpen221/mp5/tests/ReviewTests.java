package ca.ubc.ece.cpen221.mp5.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Map;

import org.junit.Test;

import ca.ece.ubc.cpen221.mp5.database.YelpDatabase;
import ca.ece.ubc.cpen221.mp5.database.YelpReview;

public class ReviewTests {
	private String restaurantJSON = "data/restaurants.json";
	private String reviewJSON = "data/reviews.json";
	private String userJSON = "data/users.json";

	@Test
	public void testReview1() { 
		YelpDatabase database = new YelpDatabase(restaurantJSON, reviewJSON, userJSON);
		YelpReview review = database.getReview("0a-pCW4guXIlWNpVeBHChg");
		
		String text = "The pizza is terrible, but if you need a place to watch a "
				+ "game or just down some pitchers, this place works.\n\nOh, and the"
				+ " pasta is even worse than the pizza.";
		database.getReview("not");
		database.getUser("not");
		
		assertEquals("0a-pCW4guXIlWNpVeBHChg", review.getReviewId());
		assertEquals("2006-07-26", review.getDate());
		assertEquals(text, review.getText());
		
		review.updateText("new text");
		assertEquals("new text", review.getText());
		assertEquals("review", review.getType());
		assertEquals("1CBs84C-a-cuA3vncXVSAw", review.getBusinessId());
		assertEquals("90wm_01FAIqhcgV_mPON9Q", review.getUserId());
		assertTrue(review.getStars() == 2);
		
		Map<String, Integer> votes = review.getVotes();
		assertTrue(votes.get("cool") == 0);
		assertTrue(votes.get("useful") == 0);
		assertTrue(votes.get("funny") == 0);
		
	}

}

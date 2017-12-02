package ca.ece.ubc.cpen221.mp5.database;

import java.util.HashMap;
import java.util.Map;

import javax.json.JsonObject;

/**
 * Rep Invariant:	Business_id, user_id, review_id are immutable
 *					Stars is an integer between 0 and 5 inclusive
 *					Votes is an non negative integer
 *					Text is a String between 0 and UPPERLIMIT characters
 *					Represents a user review for an restaurants
 * 
 * Abstraction Function: JSONobject ->  type, businessId, reviewId, text
 * stars, userId, date correspond to matching JSON tag
 * votes is a HashMap of: useful, cool, funny keys
 */
public class YelpReview implements Review {
	private String type;
	private String businessId;
	private Map<String, Integer> votes;
	private String reviewId;
	private String text;
	private double stars;
	private String userId;
	private String date;

	public YelpReview(JsonObject obj) { // NEED TO IMPLEMENET ARRAYS
		this.type = obj.getString("type");
		this.businessId = obj.getString("business_id");
		this.reviewId = obj.getString("review_id");
		this.text = obj.getString("text");
		this.stars = obj.getInt("stars");
		this.date = obj.getString("date");
		this.userId = obj.getString("user_id");

		this.votes = new HashMap<>();
		for (String category : votes.keySet()) {
			this.votes.put(category, votes.get(category));
		}
	}

	@Override
	public String getUserId() {
		return userId;
	}

	@Override
	public String getDate() {
		return date;
	}

	@Override
	public String getText() {
		return text;
	}

	@Override
	public void updateText(String text) {
		this.text = text;
	}

	// modifiers

	// getters

	public String getType() {
		return type;
	}

	public String getBusinessId() {
		return businessId;
	}

	public Map<String, Integer> getVotes() {
		Map<String, Integer> votesCopy = new HashMap<>();

		for (String category : votes.keySet()) {
			votesCopy.put(category, votes.get(category));
		}

		return votesCopy;
	}

	public String getReviewId() {
		return reviewId;
	}

	public double getStars() {
		return stars;
	}

}

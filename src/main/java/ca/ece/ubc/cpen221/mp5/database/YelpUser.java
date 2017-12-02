package ca.ece.ubc.cpen221.mp5.database;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.json.JsonObject;

/**
 * Represents a YelpUser 
 * Rep Invariant: Average_stars is the sum of all review
 * stars divided by number of reviews Review_count and votes are non-negative
 * integers User_id is immutable
 * 
 * Abstraction Function: JSONobject -> url, reviewCount, type, userId, name,
 * averagStars correspond to matching JSON tag, votes is a HashMap of: useful,
 * cool, funny keys
 */
public class YelpUser implements User {
	private String url;
	private Map<String, Integer> votes;
	private int reviewCount;
	private String type;
	private String userId;
	private String name;
	private double averageStars; // TODO update averageStars
	private List<String> reviews; // TODO move reviews to a generic in User interface??????????????

	/**
	 * Constructs the yelp user fields from the JSON object obj
	 * 
	 * @param obj
	 *            containing JSONobject
	 */
	public YelpUser(JsonObject obj) {
		this.url = obj.getString("url");
		this.reviewCount = obj.getInt("review_count");
		this.type = obj.getString("type");
		this.userId = obj.getString("user_id");
		this.name = obj.getString("name");
		this.averageStars = obj.getInt("average_stars");

		JsonObject voteJson = obj.getJsonObject("votes");
		Map<String, Integer> voteMap = new HashMap<>();
		voteMap.put("funny", voteJson.getInt("funny"));
		voteMap.put("useful", voteJson.getInt("useful"));
		voteMap.put("cool", voteJson.getInt("cool"));

		this.votes = voteMap;
		for (String category : votes.keySet()) {
			this.votes.put(category, votes.get(category));
		}

		reviews = new ArrayList<>();
	}	
	/**
	 * Adds restaurant and increment count
	 * @param review string to be added
	 */
	public void addReview(String review) {
		reviews.add(review);
		reviewCount++;
	}	
	/**
	 * Returns list of reviews
	 * @return list of reviews
	 */
	public List<String> getReviews() {
		List<String> reviewsCopy = new ArrayList<>();
		for (String review : reviews) {
			reviewsCopy.add(review);
		}
		return reviewsCopy;
	}
	/**
	 * Removes review and decrement count
	 * @param review to be removed
	 * @return boolean if review was removed
	 */
	public boolean removeReview(String review) {
		if (reviews.remove(review)) {
			reviewCount--;
			return true;
		}
		return false;
	}
	/**
	 * Returns userID
	 * @return userID
	 */
	@Override
	public String getUserId() {
		return userId;
	}
	/**
	 * Gets name
	 * @return name
	 */
	@Override
	public String getName() {
		return name;
	}
	/**
	 * Mutates name to new name
	 * @param name
	 */
	@Override
	public void setName(String name) {
		this.name = name;
	}

	/** Getters
	 * Gets url 
	 * @return url
	 */
	public String getUrl() {
		return url;
	}
	/**
	 * Gets votes map 
	 * @return map containing votes
	 */
	public Map<String, Integer> getVotes() {
		Map<String, Integer> votesCopy = new HashMap<>();

		for (String category : votes.keySet()) {
			votesCopy.put(category, votes.get(category));
		}

		return votesCopy;
	}
	/**
	 * Gets votes map copy
	 * @return map containing votes
	 */
	public Map<String, Integer> getVotesMutable() {
		Map<String, Integer> votesCopy = new HashMap<>();

		for (String category : votes.keySet()) {
			votesCopy.put(category, votes.get(category));
		}
		return votesCopy;
	}
	/**
	 * Gets review count
	 * @return review count
	 */
	public int getReviewCount() {
		return reviewCount;
	}
	/**
	 * Gets type of review
	 * @return type
	 */
	public String getType() {
		return type;
	}
	/**
	 * Gets average stars
	 * @return average stars
	 */
	public double getAverageStars() {
		return averageStars;
	}
	/**
	 * Sets average stars
	 * @param aveStars
	 */
	public void setAverageStars(double aveStars) {
		this.averageStars = aveStars;
	}
}

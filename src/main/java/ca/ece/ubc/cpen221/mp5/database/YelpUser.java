package ca.ece.ubc.cpen221.mp5.database;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class YelpUser implements User {

    private String url;
    private Map<String, Integer> votes;
    private int reviewCount;
    private String type;
    private String userId;
    private String name;
    private double averageStars;			// TODO averageStars 
    
    private List<YelpReview> reviews;		// TODO move reviews to a generic in User interface??????????????
    						// TODO yelpreview to review ids??????????
    
    public YelpUser(String url, Map<String, Integer> votes, int reviewCount, String type, String userId, String name,
	    double averageStars) {
	this.url = url;
	this.reviewCount = reviewCount;
	this.type = type;
	this.userId = userId;
	this.name = name;
	this.averageStars = averageStars;
	
	this.votes = new HashMap<>();
	for (String category : votes.keySet()) {
	    this.votes.put(category, votes.get(category));
	}
	
	reviews = new ArrayList<>();
    }

    
    public void addReview(YelpReview review) {
	reviews.add(review);
	reviewCount ++;
    }
    
    public List<YelpReview> getReviews() {
	List<YelpReview> reviewsCopy = new ArrayList<>();
	for (YelpReview review : reviews) {
	    reviewsCopy.add(review);
	}
	return reviewsCopy;
    }
    
    public boolean removeReview(YelpReview review) {
	if (reviews.remove(review)) {
	    reviewCount--;
	    return true;
	}
	return false;
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    @Override
    public String getUserId() {
	return userId;
    }

    @Override
    public String getName() {
	return name;
    }

    @Override
    public void setName(String name) {
	this.name = name;
    }

    
    // getters
    public String getUrl() {
        return url;
    }

    public Map<String, Integer> getVotes() {
	Map<String, Integer> votesCopy = new HashMap<>();
	
	for (String category : votes.keySet()){
	    votesCopy.put(category, votes.get(category));
	}
	
	return votesCopy;
    }

    public int getReviewCount() {
        return reviewCount;
    }

    public String getType() {
        return type;
    }

    public double getAverageStars() {
        return averageStars;
    }

    
    
}

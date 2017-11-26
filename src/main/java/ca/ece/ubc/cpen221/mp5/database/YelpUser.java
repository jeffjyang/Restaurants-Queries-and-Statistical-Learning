package ca.ece.ubc.cpen221.mp5.database;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.json.JsonObject;

public class YelpUser implements User {

    private String url;
    private Map<String, Integer> votes;
    private int reviewCount;
    private String type;
    private String userId;
    private String name;
    private double averageStars;			// TODO update averageStars 
    
    private List<String> reviews;		// TODO move reviews to a generic in User interface??????????????
    
    public YelpUser(JsonObject obj) {
	this.url = obj.getString("url");
	this.reviewCount = obj.getInt("reviewCount");
	this.type = obj.getString("type");
	this.userId = obj.getString("userId");
	this.name = obj.getString("name");
	this.averageStars = obj.getInt("averageStars");
	
	this.votes = new HashMap<>();
	for (String category : votes.keySet()) {
	    this.votes.put(category, votes.get(category));
	}
	
	reviews = new ArrayList<>();
    }

    
    public void addReview(String review) {
	reviews.add(review);
	reviewCount ++;
    }
    
    public List<String> getReviews() {
	List<String> reviewsCopy = new ArrayList<>();
	for (String review : reviews) {
	    reviewsCopy.add(review);
	}
	return reviewsCopy;
    }
    
    public boolean removeReview(String review) {
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

package ca.ece.ubc.cpen221.mp5.database;

import java.util.HashMap;
import java.util.Map;

import javax.json.JsonObject;

public class YelpReview implements Review{

    private String review;
    private String businessId;
    private Map<String, Integer> votes;
    private String reviewId;		
    private String text;
    private int stars;
    private String userId;		
    private String date;
    

    
    public YelpReview(JsonObject obj) { //NEED TO IMPLEMENET ARRAYS 
	this.review = obj.getString("type");
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
    
    
    public String getReview() {
        return review;
    }

    public String getBusinessId() {
        return businessId;
    }


    public Map<String, Integer> getVotes() {
	Map<String, Integer> votesCopy = new HashMap<>();
	
	for (String category : votes.keySet()){
	    votesCopy.put(category, votes.get(category));
	}
	
	return votesCopy;
    }

    public String getReviewId() {
        return reviewId;
    }

    public int getStars() {
        return stars;
    }
    
    
    
    
    
}

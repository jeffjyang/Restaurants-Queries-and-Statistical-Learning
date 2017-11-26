package ca.ece.ubc.cpen221.mp5.database;

import java.util.HashMap;
import java.util.Map;

public class YelpReview implements Review{

    private String review;
    private String businessId;
    private Map<String, Integer> votes;
    private String reviewId;		
    private String text;
    private int stars;
    private String userId;		
    private String date;
    
 
    
    public YelpReview() {}
    
    public YelpReview(String review, String businessId, Map<String, Integer> votes, String reviewId, String text,
	    int stars, String userId, String date) {
	this.review = review;
	this.businessId = businessId;
	this.reviewId = reviewId;
	this.text = text;
	this.stars = stars;
	this.date = date;
	this.userId = userId;

	
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

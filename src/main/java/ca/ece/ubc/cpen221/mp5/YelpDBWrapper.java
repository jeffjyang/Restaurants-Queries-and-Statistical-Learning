package ca.ece.ubc.cpen221.mp5;

import java.util.Set;

import ca.ece.ubc.cpen221.mp5.database.YelpDatabase;
import ca.ece.ubc.cpen221.mp5.database.YelpRestaurant;

// singleton thread safe wrapper for YelpDB
public class YelpDBWrapper {
    private String restaurantJSON = "data/restaurants.json";
    private String reviewJSON = "data/reviews.json";
    private String userJSON = "data/users.json";
    private YelpDatabase database;

    private final String restaurantNotExistErr = "ERR: INVALID_BUSINESS_ID";
    private final String addUserErr = "ERR: INVALID_USER_STRING";
    private final String addRestaurantErr = "ERR: INVALID_RESTAURANT_STRING";
    private final String addReviewInvalidErr = "ERR: INVALID_REVIEW_STRING";
    private final String addReviewRestaurantErr = "ERR: NO_SUCH_RESTAURANT";
    private final String addReviewUserErr = "ERR: NO_SUCH_USER";
    private final String getQueryInvalidErr = "ERR: INVALID_QUERY";
    private final String getQueryMatchErr = "ERR: NO_MATCH";
    
    private static YelpDBWrapper instance; 

    private YelpDBWrapper() {
	database = new YelpDatabase(restaurantJSON, reviewJSON, userJSON);
    }

    public static YelpDBWrapper getInstance() {
	if (instance == null) {
	    return createInstance();
	}

	return instance;
    }

    // TODO verify this is right 
    private static synchronized YelpDBWrapper createInstance() {
	if (instance == null) {
	    instance = new YelpDBWrapper();
	}
	return instance;
    }

    public synchronized String getRestaurant(String businessId) {
	String restaurantJson = database.getRestaurantJson(businessId);
	if ("".equals(restaurantJson)) {
	    return restaurantNotExistErr;
	}
	return restaurantJson;
    }

    public synchronized String addUser(String userInfo) {
	String newUserJson = database.adduser(userInfo);
	if ("".equals(newUserJson)) {
	    return addUserErr;
	}
	return newUserJson;
    }

    public synchronized String addRestaurant(String restaurantInfo) {
	String newRestaurant = database.addRestaurant(restaurantInfo);
	if ("".equals(newRestaurant)) {
	    return addRestaurantErr;
	}
	return newRestaurant;
    }

    public synchronized String addReview(String reviewInfo) {
	String newReview = database.addRestaurant(reviewInfo);
	if ("".equals(newReview)) {
	    return addReviewInvalidErr;
	}
	if ("1".equals(newReview)) {
	    return addReviewRestaurantErr;
	}
	if ("2".equals(newReview)) {
	    return addReviewUserErr;
	}	

	return newReview;

    }

    public synchronized String getQuery(String query) {
	Set<YelpRestaurant> restaurantMatches = database.getMatches(query);

	if (restaurantMatches == null) {
	    return getQueryInvalidErr;
	}
	if (restaurantMatches.isEmpty()) {
	    return getQueryMatchErr;
	}
	
	String matches = "";

	// TODO error codes
	for (YelpRestaurant restaurant : restaurantMatches) {
	    matches += restaurant.getName() + ", ";
	}

	return matches;
    }





}

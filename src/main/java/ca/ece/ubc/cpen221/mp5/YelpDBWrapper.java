package ca.ece.ubc.cpen221.mp5;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Set;
import java.util.Map.Entry;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonReader;
import javax.json.JsonValue;

import ca.ece.ubc.cpen221.mp5.database.YelpDatabase;
import ca.ece.ubc.cpen221.mp5.database.YelpRestaurant;
import ca.ece.ubc.cpen221.mp5.database.YelpReview;
import ca.ece.ubc.cpen221.mp5.database.YelpUser;

/**\
 * A singleton thread safe wrapper for YelpDatabase
 * 
 * Rep Invariant: Only max one instance of YelpDBWrapper is present at any given time
 * 		All rep invariants of YelpDatabase
 * 
 * Abstraction Function: String JSONRestauant -> Set of YelpRestaurant Objects
 * String JSONReviews -> Set of YelpReview Object String JSONUser -> Set of
 * YelpUser Object
 */
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

    
    /**
     * Returns the one instance of YelpDBWrapper. If the instance has not been created 
     * yet, this will first create it then return it 
     * @return the instance of YelpDBWrapper
     */
    public static YelpDBWrapper getInstance() {
	if (instance == null) {
	    return createInstance();
	}

	return instance;
    }

    
    private static synchronized YelpDBWrapper createInstance() {
	if (instance == null) {
	    instance = new YelpDBWrapper();
	}
	return instance;
    }

    /**
     * Returns the JSON string of the restaurant represented by businessID 
     * @param businessId
     * 		the business_id of the restaurant we want the JSON string of
     * @return the JSON string of the restaurant represented by businessId
     */
    public synchronized String getRestaurant(String businessId) {
	YelpRestaurant restaurant = database.getRestaurant(businessId);
	if (restaurant == null) {
	    return restaurantNotExistErr;
	}
	return restaurant.getJsonString();	
    }



    /**
     * Adds a user to the database 
     * @param userInfo
     * 		The new user information in JSON format
     * 		Requires: userInfo contains the field "name" and the field has a string attached to it
     * @return the JSON representation of the user that was added
     */
    public synchronized String addUser(String userInfo) {


	InputStream is = new ByteArrayInputStream(userInfo.getBytes());
	JsonReader reader = Json.createReader(is);
	JsonObject userInput;
	try {
	    userInput = reader.readObject();
	} catch (Exception e) {
	    return addUserErr;
	}

	try {
	    if (userInput.isNull("name")) {
		return addUserErr;
	    }
	} catch (Exception e) {
	    return addUserErr;

	}
	String name;
	try {
	    name = userInput.getString("name");
	} catch (Exception e) {
	    return addUserErr;
	}
	String url = "google.com/eyylmao this is totally a url";
	String userId = "TotallyARandomStringLel";

	JsonObject reviewCount = Json.createObjectBuilder()
		.add("funny", 0)
		.add("useful", 0)
		.add("cool", 0)
		.build();

	JsonObject newUserJson = Json.createObjectBuilder()
		.add("url", url)
		.add("votes", reviewCount) // TODO verify json 
		.add("review_count", 0)
		.add("type", "user")
		.add("user_id", userId)
		.add("name", name)
		.add("average_stars", 0)
		.build();

	YelpUser newUser = new YelpUser(newUserJson);

	database.addUser(newUser);

	return newUserJson.toString();
    }

    /**
     * Adds a new restaurant to the database 
     * @param restaurantInfo
     * 		information of the new restaurant in JSON format 
     * @return the JSON representation of the restaurant that was added
     */
    public synchronized String addRestaurant(String restaurantInfo) {


	InputStream is = new ByteArrayInputStream(restaurantInfo.getBytes());
	JsonReader reader = Json.createReader(is);
	JsonObject restaurantJson;
	try {
	    restaurantJson = reader.readObject();
	} catch (Exception e) {
	    return addRestaurantErr;
	}
	// TODO check for error in fields

	try {
	    if (restaurantJson.isNull("open") || 
		    restaurantJson.isNull("url") ||
		    restaurantJson.isNull("longitude") ||
		    restaurantJson.isNull("neighborhoods") ||
		    restaurantJson.isNull("name") ||
		    restaurantJson.isNull("categories") ||
		    restaurantJson.isNull("state") ||
		    restaurantJson.isNull("type") ||
		    restaurantJson.isNull("city") ||
		    restaurantJson.isNull("full_address") ||
		    restaurantJson.isNull("review_count") ||
		    restaurantJson.isNull("photo_url") ||
		    restaurantJson.isNull("schools") ||
		    restaurantJson.isNull("latitude") ||
		    restaurantJson.isNull("price")
		    ) 
	    {
		return addRestaurantErr;
	    }

	} catch (Exception e) {
	    return addRestaurantErr;

	}
	try {
	    String type = restaurantJson.getString("type");
	    if (!"business".equals(type.toLowerCase())) {
		return addRestaurantErr;
	    }

	    int reviewCount = restaurantJson.getInt("review_count");
	    if (reviewCount < 0) {
		return addRestaurantErr;
	    }

	    int price = restaurantJson.getInt("price");
	    if (price < 0 || price > 4) {
		return addRestaurantErr;
	    }



	} catch (Exception e) {
	    return addRestaurantErr;
	}



	String businessId = "VerySecureRandomString";

	JsonObjectBuilder newRestaurantBuilder = Json.createObjectBuilder();

	for (Entry<String, JsonValue> property : restaurantJson.entrySet()) {
	    newRestaurantBuilder.add(property.getKey(), property.getValue());
	}

	newRestaurantBuilder.add("business_id", businessId);
	newRestaurantBuilder.add("stars", 0);
	JsonObject newRestaurantJson = newRestaurantBuilder.build();

	YelpRestaurant newRestaurant = new YelpRestaurant(newRestaurantJson);

	database.addRestaurant(newRestaurant);


	return newRestaurantJson.toString();
    }

    /**
     * Adds a new review to the database 
     * @param reviewInfo
     * 		information of the new review in JSON format 
     * @return the JSON representation of the review that was added
     */
    public synchronized String addReview(String reviewInfo) {


	InputStream is = new ByteArrayInputStream(reviewInfo.getBytes());
	JsonReader reader = Json.createReader(is);
	JsonObject reviewJson;
	try {
	    reviewJson = reader.readObject();
	} catch (Exception e) {
	    return addReviewInvalidErr;
	}

	try {
	    if (reviewJson.isNull("type") || 
		    reviewJson.isNull("business_id") ||
		    reviewJson.isNull("text") ||
		    reviewJson.isNull("stars") ||
		    reviewJson.isNull("user_id") ||
		    reviewJson.isNull("date")
		    ) 
	    {
		return addReviewInvalidErr;
	    }
	} catch (Exception e) {
	    return addReviewInvalidErr;

	}

	try {
	    String type = reviewJson.getString("type");
	    if (!"review".equals(type.toLowerCase())) {
		return addReviewInvalidErr;
	    }	    
	    int stars = reviewJson.getInt("stars");
	    if (stars < 0 || stars > 5) {
		return addReviewInvalidErr;
	    }
	} catch (Exception e) {
	    return addReviewInvalidErr;
	}

	String businessId = reviewJson.getString("business_id");
	String userId = reviewJson.getString("user_id");

	if (!database.containsRestaurant(businessId)) {
	    return addReviewRestaurantErr;
	}
	if (!database.containsUser(userId)) {
	    return addReviewUserErr;
	}

	JsonObject reviewVotes = Json.createObjectBuilder()
		.add("funny", 0)
		.add("useful", 0)
		.add("cool", 0)
		.build();



	String reviewId = "VerySecureRandomReviewStringIDxD";

	JsonObjectBuilder newReviewBuilder = Json.createObjectBuilder();

	for (Entry<String, JsonValue> property : reviewJson.entrySet()) {
	    newReviewBuilder.add(property.getKey(), property.getValue());
	}

	newReviewBuilder.add("review_id", reviewId);
	newReviewBuilder.add("votes", reviewVotes);

	JsonObject newReviewJson = newReviewBuilder.build();

	YelpReview newReview = new YelpReview(newReviewJson);

	database.addReview(newReview);

	return newReviewJson.toString();


    }

    /**
     * Performs the given query on the database
     * @param query
     * 		The query we wish to perform
     * 		Requires: query follows the grammer rules
     * @return the results of the query
     */
    public synchronized String getQuery(String query) {
	Set<YelpRestaurant> restaurantMatches;
	try {
	    restaurantMatches = database.getMatches(query);
	} catch (Exception e) {
	    return getQueryInvalidErr;
	}
	
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

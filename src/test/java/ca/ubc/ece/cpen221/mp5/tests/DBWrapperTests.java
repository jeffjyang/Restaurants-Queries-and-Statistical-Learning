package ca.ubc.ece.cpen221.mp5.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonValue;

import org.junit.Test;

import ca.ece.ubc.cpen221.mp5.YelpDBWrapper;

public class DBWrapperTests {

    private final String restaurantNotExistErr = "ERR: INVALID_BUSINESS_ID";
    private final String addUserErr = "ERR: INVALID_USER_STRING";
    private final String addRestaurantErr = "ERR: INVALID_RESTAURANT_STRING";
    private final String addReviewInvalidErr = "ERR: INVALID_REVIEW_STRING";
    private final String addReviewRestaurantErr = "ERR: NO_SUCH_RESTAURANT";
    private final String addReviewUserErr = "ERR: NO_SUCH_USER";
    private final String getQueryInvalidErr = "ERR: INVALID_QUERY";
    private final String getQueryMatchErr = "ERR: NO_MATCH";

    // test singleton
    @Test
    public void test00() {
	YelpDBWrapper instance1 = YelpDBWrapper.getInstance();
	YelpDBWrapper instance2 = YelpDBWrapper.getInstance();
	assertTrue(instance1 == instance2);
    }

    // getRestaurant
    @Test
    public void test01() {
	YelpDBWrapper database = YelpDBWrapper.getInstance();
	
	String restaurantError = database.getRestaurant("not a busines id");
	assertEquals(restaurantNotExistErr, restaurantError);
	
	String validRestaurant = database.getRestaurant("BJKIoQa5N2T_oDlLVf467Q");
	InputStream is = new ByteArrayInputStream(validRestaurant.getBytes());
	JsonReader reader = Json.createReader(is);
	JsonObject restaurantJson = null;
	try {
	    restaurantJson = reader.readObject();
	} catch (Exception e) {
	    fail();
	}
	
	try {
	    if (!"Jasmine Thai".equals(restaurantJson.getString("name"))) {
		fail();
	    }
	} catch (Exception e) {
	    fail();
	}


    }

    // addUser
    @Test
    public void test02() {
	YelpDBWrapper database = YelpDBWrapper.getInstance();

	JsonObject validUserJson = Json.createObjectBuilder().add("name", "My name!!")
		.add("badField", "this should be ignored").add("url", "this should be ignored").build();

	InputStream is = new ByteArrayInputStream(database.addUser(validUserJson.toString()).getBytes());
	JsonReader reader = Json.createReader(is);
	JsonObject createdUser = null;
	try {
	    createdUser = reader.readObject();
	} catch (Exception e) {
	    fail();
	}

	if (createdUser.isNull("url") || createdUser.isNull("votes") || createdUser.isNull("review_count")
		|| createdUser.isNull("type") || createdUser.isNull("user_id") || createdUser.isNull("name")
		|| createdUser.isNull("average_stars")) {
	    fail();
	}

	JsonObject invalidUserJson = Json.createObjectBuilder().add("badField", "theres no name!")
		.add("url", "this should be ignored").build();

	assertEquals(addUserErr, database.addUser(invalidUserJson.toString()));

	assertEquals(addUserErr, database.addUser("invalid JSON string"));

	JsonObject invalidUserJson2 = Json.createObjectBuilder().add("name", JsonValue.NULL)
		.add("badField", "this should be ignored").add("url", "this should be ignored").build();
	
	assertEquals(addUserErr, database.addUser(invalidUserJson2.toString()));

	JsonObject invalidUserJson3 = Json.createObjectBuilder().add("name", 2)
		.add("badField", "this should be ignored").add("url", "this should be ignored").build();
   
	assertEquals(addUserErr, database.addUser(invalidUserJson3.toString()));

    }

    // addRestaurant
    @Test
    public void test03() {
	YelpDBWrapper database = YelpDBWrapper.getInstance();

	JsonArray jsonArray = Json.createArrayBuilder()
		.add("Mr Rogers")
		.build();
	
	JsonObject validRestaurantJson = Json.createObjectBuilder().add("open", true)
		.add("url", "www. this is a url!! .com").add("longitude", 404).add("neighborhoods", jsonArray)
		.add("name", "a name!").add("categories", jsonArray).add("state", "a state").add("type", "business")
		.add("city", "a city").add("full_address", "123 sesame street").add("review_count", 2)
		.add("photo_url", "url").add("schools", jsonArray).add("latitude", 404).add("price", 3).build();

	InputStream is = new ByteArrayInputStream(database.addRestaurant(validRestaurantJson.toString()).getBytes());
	JsonReader reader = Json.createReader(is);
	JsonObject createdRestaurant = null;
	try {
	    createdRestaurant = reader.readObject();
	} catch (Exception e) {
	    fail();
	}

	try {
	    if (createdRestaurant.isNull("open") || createdRestaurant.isNull("url")
		    || createdRestaurant.isNull("longitude") || createdRestaurant.isNull("neighborhoods")
		    || createdRestaurant.isNull("name") || createdRestaurant.isNull("categories")
		    || createdRestaurant.isNull("state") || createdRestaurant.isNull("type")
		    || createdRestaurant.isNull("city") || createdRestaurant.isNull("full_address")
		    || createdRestaurant.isNull("review_count") || createdRestaurant.isNull("photo_url")
		    || createdRestaurant.isNull("schools") || createdRestaurant.isNull("latitude")
		    || createdRestaurant.isNull("price") || createdRestaurant.isNull("business_id")
		    || createdRestaurant.isNull("stars")) {
		fail();
	    }
	} catch (Exception e) {
	    fail();
	}

	JsonObject invalidRestaurantJson = Json.createObjectBuilder().add("open", true)
		.add("url", "www. this is a url!! .com").add("longitude", 404).add("neighborhoods", jsonArray)
		.add("name", "a name!").add("categories", jsonArray).add("state", "a state").add("type", "not a business")
		.add("city", "a city").add("full_address", "123 sesame street").add("review_count", 2)
		.add("photo_url", "url").add("schools", jsonArray).add("latitude", 404).add("price", 3).build();

	assertEquals(addRestaurantErr, database.addRestaurant(invalidRestaurantJson.toString()));

	JsonObject invalidRestaurantJson2 = Json.createObjectBuilder().add("open", true)
		.add("url", "www. this is a url!! .com").add("longitude", 404).add("neighborhoods", jsonArray)
		.add("name", "a name!").add("categories", jsonArray).add("state", "a state").add("type", "business")
		.add("city", "a city").add("full_address", "123 sesame street").add("review_count", 2)
		.add("photo_url", "url").add("schools", JsonValue.NULL).add("latitude", 404).add("price", 3).build();
	

	assertEquals(addRestaurantErr, database.addRestaurant(invalidRestaurantJson2.toString()));

	JsonObject invalidRestaurantJson3 = Json.createObjectBuilder().add("open", true)
		.add("url", "www. this is a url!! .com").add("longitude", 404).add("neighborhoods", jsonArray)
		.add("name", "a name!").add("categories", jsonArray).add("state", "a state").add("type", "business")
		.add("city", "a city").add("full_address", "123 sesame street").add("review_count", 2)
		.add("photo_url", "url").add("schools", jsonArray).add("latitude", 404).add("price", 6).build();
	
	assertEquals(addRestaurantErr, database.addRestaurant(invalidRestaurantJson3.toString()));

	JsonObject invalidRestaurantJson4 = Json.createObjectBuilder().add("open", true)
		.add("url", "www. this is a url!! .com").add("longitude", 404).add("neighborhoods", jsonArray)
		.add("name", "a name!").add("categories", jsonArray).add("state", "a state").add("type", "business")
		.add("city", "a city").add("full_address", "123 sesame street").add("review_count", -2)
		.add("photo_url", "url").add("schools", jsonArray).add("latitude", 404).add("price", 3).build();

	assertEquals(addRestaurantErr, database.addRestaurant(invalidRestaurantJson4.toString()));
	
	assertEquals(addRestaurantErr, database.addRestaurant("totally json"));
	
	JsonObject invalidRestaurantJson5 = Json.createObjectBuilder().add("open", true)
		.add("url", JsonValue.NULL).add("longitude", 404).add("neighborhoods", jsonArray)
		.add("name", "a name!").add("categories", jsonArray).add("state", "a state").add("type", "business")
		.add("city", "a city").add("full_address", "123 sesame street").add("review_count", "AN INTEGER NOT!")
		.add("photo_url", "url").add("schools", jsonArray).add("latitude", 404).add("price", 3).build();

	assertEquals(addRestaurantErr, database.addRestaurant(invalidRestaurantJson5.toString()));

	
	JsonObject invalidRestaurantJson6 = Json.createObjectBuilder().add("open", true)
		.add("url", "www. this is a url!! .com").add("longitude", 404).add("neighborhoods", jsonArray)
		.add("name", "a name!").add("categories", jsonArray).add("state", "a state").add("type", "business")
		.add("city", "a city").add("full_address", "123 sesame street").add("review_count", 2)
		.add("photo_url", "url").add("schools", jsonArray).add("latitude", 404).add("price", "THIS SHOULD FAIL").build();
   
	assertEquals(addRestaurantErr, database.addRestaurant(invalidRestaurantJson6.toString()));

	JsonObject invalidRestaurantJson7 = Json.createObjectBuilder().add("open", true)
		.add("url", "www. this is a url!! .com").add("longitude", 404).add("neighborhoods", jsonArray)
		.add("name", "a name!").add("categories", jsonArray).add("state", "a state").add("type", "business")
		.add("city", "a city").add("full_address", "123 sesame street").add("review_count", 2)
		.add("photo_url", "url").add("schools", jsonArray).add("latitude", 404).build();
	
	assertEquals(addRestaurantErr, database.addRestaurant(invalidRestaurantJson7.toString()));

    }
    
    // test addReview
    @Test
    public void test04() {
	YelpDBWrapper database = YelpDBWrapper.getInstance();

	JsonObject validReviewJson = Json.createObjectBuilder()
		.add("type", "review")
		.add("business_id",  "1CBs84C-a-cuA3vncXVSAw")
		.add("text", "text")
		.add("stars", 2)
		.add("user_id", "90wm_01FAIqhcgV_mPON9Q")
		.add("date", "2006-07-26")
		.build();
	
	InputStream is = new ByteArrayInputStream(database.addReview(validReviewJson.toString()).getBytes());
	JsonReader reader = Json.createReader(is);
	JsonObject createdReview = null;
	try {
	    createdReview = reader.readObject();
	} catch (Exception e) {
	    fail();
	}
	try {
	    if (createdReview.isNull("type") || 
		    createdReview.isNull("business_id") ||
		    createdReview.isNull("text") ||
		    createdReview.isNull("stars") ||
		    createdReview.isNull("user_id") ||
		    createdReview.isNull("date") ||
		    createdReview.isNull("votes") ||
		    createdReview.isNull("review_id")
		    ) 
	    {
		fail();
	    }
	} catch (Exception e) {
	    fail();

	}
	

	JsonObject invalidReviewNoBusiness = Json.createObjectBuilder()
		.add("type", "review")
		.add("business_id",  "not a business")
		.add("text", "text")
		.add("stars", 2)
		.add("user_id", "90wm_01FAIqhcgV_mPON9Q")
		.add("date", "2006-07-26")
		.build();
	
	assertEquals(addReviewRestaurantErr, database.addReview(invalidReviewNoBusiness.toString()));
	
	JsonObject invalidReview = Json.createObjectBuilder()
		.add("type", "not a review")
		.add("business_id",  "1CBs84C-a-cuA3vncXVSAw")
		.add("text", "text")
		.add("stars", 2)
		.add("user_id", "90wm_01FAIqhcgV_mPON9Q")
		.add("date", "2006-07-26")
		.build();
	
	assertEquals(addReviewInvalidErr, database.addReview(invalidReview.toString()));
	
	JsonObject invalidReview2 = Json.createObjectBuilder()
		.add("type", "review")
		.add("business_id",  "1CBs84C-a-cuA3vncXVSAw")
		.add("text", "text")
		.add("stars", -2)
		.add("user_id", "90wm_01FAIqhcgV_mPON9Q")
		.add("date", "2006-07-26")
		.build();
	
	assertEquals(addReviewInvalidErr, database.addReview(invalidReview2.toString()));
	
	
	JsonObject invalidReview3 = Json.createObjectBuilder()
		.add("type", 404)
		.add("business_id",  "1CBs84C-a-cuA3vncXVSAw")
		.add("text", "text")
		.add("stars", -2)
		.add("user_id", "90wm_01FAIqhcgV_mPON9Q")
		.add("date", "2006-07-26")
		.build();
	
	assertEquals(addReviewInvalidErr, database.addReview(invalidReview3.toString()));
	
	JsonObject invalidReview4 = Json.createObjectBuilder()
		.add("type", "review")
		.add("business_id",  "1CBs84C-a-cuA3vncXVSAw")
		.add("text", "text")
		.add("stars", 2)
		.add("user_id", "no user")
		.add("date", "2006-07-26")
		.build();
	
	assertEquals(addReviewUserErr, database.addReview(invalidReview4.toString()));
	
	
	JsonObject invalidReview5 = Json.createObjectBuilder()
		.add("type", "review")
		.add("business_id",  "1CBs84C-a-cuA3vncXVSAw")
		.add("text", "text")
		.add("stars", 2)
		.add("user_id", "90wm_01FAIqhcgV_mPON9Q")
		.build();
	
	assertEquals(addReviewInvalidErr, database.addReview(invalidReview5.toString()));
	
	
	JsonObject invalidReview6 = Json.createObjectBuilder()
		.add("type", JsonValue.NULL)
		.add("business_id",  "1CBs84C-a-cuA3vncXVSAw")
		.add("text", "text")
		.add("stars", 2)
		.add("user_id", "90wm_01FAIqhcgV_mPON9Q")
		.build();
	
	assertEquals(addReviewInvalidErr, database.addReview(invalidReview6.toString()));

	assertEquals(addReviewInvalidErr, database.addReview("totally JSON"));

    }
    
    // getQuery
    @Test
    public void test05() {
	YelpDBWrapper database = YelpDBWrapper.getInstance();
	
	String query = "in(Telegraph Ave) && (category(Chinese) || category(Italian)) && price <= 2";
	assertTrue(database.getQuery(query).length() > 0);
	
	String invalidQuery = "not a valid query";
	assertEquals(getQueryInvalidErr, database.getQuery(invalidQuery));
	
	String noResultQuery = "in(Telegraph Ave) && (category(Chinese) && category(Italian)) && price >= 3";
	assertEquals(getQueryMatchErr, database.getQuery(noResultQuery));
	
	String invalidQuery2 = "in(Telegraph Ave) && (category(Chinese) && category(Italian)) && price >= 7";
	assertEquals(getQueryInvalidErr, database.getQuery(invalidQuery2));
	
    }

}

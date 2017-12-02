package ca.ubc.ece.cpen221.mp5.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;

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

	String validRestaurant = database.getRestaurant("BJKIoQa5N2T_oDlLVf467Q");

	assertEquals("{\"open\": true, \"url\": \"http://www.yelp.com/biz/jasmine-thai-berkeley\", "
		+ "\"longitude\": -122.2602981, \"neighborhoods\": [\"UC Campus Area\"], \"business_id\": "
		+ "\"BJKIoQa5N2T_oDlLVf467Q\", \"name\": \"Jasmine Thai\", \"categories\": [\"Thai\", \"Restaurants\"], "
		+ "\"state\": \"CA\", \"type\": \"business\", \"stars\": 3.0, \"city\": \"Berkeley\", \"full_address\": "
		+ "\"1805 Euclid Ave\\nUC Campus Area\\nBerkeley, CA 94709\", \"review_count\": 52, \"photo_url\": "
		+ "\"http://s3-media2.ak.yelpcdn.com/bphoto/ZwTUUb-6jkuzMDBBsUV6Eg/ms.jpg\", \"schools\": "
		+ "[\"University of California at Berkeley\"], \"latitude\": 37.8759615, \"price\": 2}",
		validRestaurant);

	String restaurantError = database.getRestaurant("not a busines id");

	assertEquals(restaurantNotExistErr, restaurantError);
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

	JsonObject inValidUserJson = Json.createObjectBuilder().add("badField", "theres no name!")
		.add("url", "this should be ignored").build();

	assertEquals(addUserErr, database.addUser(inValidUserJson.toString()));

	assertEquals(addUserErr, database.addUser("invalid JSON string"));

    }

    // addRestaurant
    @Test
    public void test03() {
	YelpDBWrapper database = YelpDBWrapper.getInstance();

	JsonObject validRestaurantJson = Json.createObjectBuilder().add("open", "true")
		.add("url", "www. this is a url!! .com").add("longitude", 404).add("neighborhoods", "not null")
		.add("name", "a name!").add("categories", "not null").add("state", "a state").add("type", "business")
		.add("city", "a city").add("full_addresss", "123 sesame street").add("review_count", 2)
		.add("photo_url", "url").add("schools", "not null").add("latitude", 404).add("price", 3).build();

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

	JsonObject inValidRestaurantJson = Json.createObjectBuilder().add("open", "true")
		.add("url", "www. this is a url!! .com").add("longitude", 404).add("neighborhoods", "not null")
		.add("name", "a name!").add("categories", "not null").add("type", "not a business O.O")
		.add("state", "a state").add("city", "a city").add("full_addresss", "123 sesame street")
		.add("review_count", 2).add("photo_url", "url").add("schools", "not null").add("latitude", 404)
		.add("price", 3).build();

	assertEquals(addRestaurantErr, database.addRestaurant(inValidRestaurantJson.toString()));

	JsonObject inValidRestaurantJson2 = Json.createObjectBuilder().add("open", "true")
		.add("url", "www. this is a url!! .com").add("longitude", 404).add("neighborhoods", "not null")
		.add("name", "a name!").add("categories", "not null").add("type", "business").add("state", "a state")
		.add("city", "a city").add("full_addresss", "123 sesame street").add("review_count", 2)
		.add("photo_url", "url").add("schools", "not null").add("latitude", 404).add("price", 3).build();

	assertEquals(addRestaurantErr, database.addRestaurant(inValidRestaurantJson2.toString()));

	JsonObject inValidRestaurantJson3 = Json.createObjectBuilder().add("open", "true")
		.add("url", "www. this is a url!! .com").add("longitude", 404).add("neighborhoods", "not null")
		.add("name", "a name!").add("categories", "not null").add("state", "a state").add("type", "business")
		.add("city", "a city").add("full_addresss", "123 sesame street").add("review_count", 2)
		.add("photo_url", "url").add("schools", "not null").add("latitude", 404).add("price", 6).build();

	assertEquals(addRestaurantErr, database.addRestaurant(inValidRestaurantJson3.toString()));

	JsonObject inValidRestaurantJson4 = Json.createObjectBuilder().add("open", "true")
		.add("url", "www. this is a url!! .com").add("longitude", 404).add("neighborhoods", "not null")
		.add("name", "a name!").add("categories", "not null").add("state", "a state").add("type", "business")
		.add("city", "a city").add("full_addresss", "123 sesame street").add("review_count", -2)
		.add("photo_url", "url").add("schools", "not null").add("latitude", 404).add("price", 3).build();

	assertEquals(addRestaurantErr, database.addRestaurant(inValidRestaurantJson4.toString()));

    }

}

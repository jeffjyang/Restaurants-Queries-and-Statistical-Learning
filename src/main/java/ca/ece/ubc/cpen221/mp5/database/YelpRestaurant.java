package ca.ece.ubc.cpen221.mp5.database;

import java.util.ArrayList;
import java.util.List;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;

/**
 * Represents YelpRestaurant containing JSON fields with Yelp data set
 * 
 * Rep Invariants: Is not null Latitude, longitude will not change and are valid
 * locations within the city Stars is between 0 and 5 inclusive Price is an
 * integer between 1 and 4 inclusive Business_id will not change Review_count is
 * greater or equal to 0 Schools will not change
 *
 * Abstraction Function: JSONobject -> open, url, longitude, businessId, name,
 * state, type, stars, city, fullAddress, reviewCount, photoUrl, latitude
 * matches the corresponding JSON tag. Neighborhoods, categories, schools are
 * ArrayLists of the JSONArray data
 *
 */
public class YelpRestaurant implements Business {
	private boolean open;
	private String url;
	private double longitude;
	private List<String> neighborhoods = new ArrayList<>();
	private String businessId;
	private String name;
	private List<String> categories = new ArrayList<>();
	private String state;
	private String type;
	private double stars; // TODO update stars
	private String city;
	private String fullAddress;
	private int reviewCount;
	private String photoUrl;
	private List<String> schools = new ArrayList<>();
	private double latitude;
	private int price;
	private List<String> reviews; // TODO change YelpReview to review ids?

	/**
	 * Constructs the restaurant fields from JSON object
	 * 
	 * @param obj
	 *            of restaruant JSON
	 */
	public YelpRestaurant(JsonObject obj) {
		this.open = (Boolean) obj.getBoolean("open");
		this.url = obj.getString("url");
		this.longitude = Double.parseDouble(obj.get("longitude").toString());
		this.businessId = obj.getString("business_id");
		this.name = obj.getString("name");
		this.state = obj.getString("state");
		this.type = obj.getString("type");
		this.stars = obj.getJsonNumber("stars").doubleValue();
		this.city = obj.getString("city");
		this.fullAddress = obj.getString("full_address");
		this.reviewCount = obj.getInt("review_count");
		this.photoUrl = obj.getString("photo_url");
		this.latitude = Double.parseDouble(obj.get("latitude").toString());
		this.price = obj.getInt("price");

		JsonArray neighborhoodJson = obj.getJsonArray("neighborhoods");
		if (neighborhoodJson != null) {
			for (int index = 0; index < neighborhoodJson.size(); index++) {
				this.neighborhoods.add(neighborhoodJson.get(index).toString().replace("\"", ""));
			}
		}

		JsonArray schoolsJson = obj.getJsonArray("schools");
		if (schoolsJson != null) {
			for (int index = 0; index < schoolsJson.size(); index++) {
				this.schools.add(schoolsJson.get(index).toString().replace("\"", ""));
			}
		}

		JsonArray categoriesJson = obj.getJsonArray("categories");
		if (categoriesJson != null) {
			for (int index = 0; index < categoriesJson.size(); index++) {
				this.categories.add(categoriesJson.get(index).toString().replace("\"", ""));
			}
		}
		reviews = new ArrayList<>();
	}

	/**
	 * Returns the JSON string from the restaurant fields
	 * 
	 * @return returns JSON string
	 */
	public String getJsonString() {
		JsonArrayBuilder neighborhoodsJsonBuilder = Json.createArrayBuilder();
		for (String neighborhood : neighborhoods) {
			neighborhoodsJsonBuilder.add(neighborhood);
			System.out.println(neighborhood);
		}
		JsonArray neighborhoodsJson = neighborhoodsJsonBuilder.build();

		JsonArrayBuilder categoriesJsonBuilder = Json.createArrayBuilder();
		for (String catageory : categories) {
			categoriesJsonBuilder.add(catageory);
		}
		JsonArray categoriesJson = categoriesJsonBuilder.build();

		JsonArrayBuilder schoolsJsonBuilder = Json.createArrayBuilder();
		for (String school : schools) {
			schoolsJsonBuilder.add(school);
		}
		JsonArray schoolsJson = schoolsJsonBuilder.build();

		JsonObject restaurantJson = Json.createObjectBuilder().add("open", this.open) // TODO: Confirm x is longitude
				.add("url", this.url).add("longitude", this.longitude).add("neighborhoods", neighborhoodsJson)// .toString())
				.add("business_id", this.businessId) // TODO: What is cluster weight?
				.add("name", this.name).add("categories", categoriesJson).add("state", this.state)
				.add("type", this.type).add("city", this.city).add("full_address", this.fullAddress)
				.add("review_count", this.reviewCount).add("photo_url", this.photoUrl).add("schools", schoolsJson)
				.add("latitude", this.latitude).add("price", this.price).build();

		return restaurantJson.toString();
	}

	/**
	 * Adds review to the list of reviews
	 * 
	 * @param review
	 *            to be added
	 */
	// NOTE DOES NOT UPDATE RATING
	public void addReview(String review) {
		reviews.add(review);
		reviewCount++;
	}

	/**
	 * Returns list of strings with all the reviews
	 * 
	 * @return reviews strings
	 */
	public List<String> getReviews() {
		List<String> reviewsCopy = new ArrayList<>(reviews);

		return reviewsCopy;
	}

	/**
	 * Remove the review from list of reviews
	 * 
	 * @param review
	 *            to be removed
	 * @return boolean if successful or false
	 */
	public boolean removeReview(String review) {
		if (reviews.remove(review)) {
			reviewCount--;
			return true;
		}
		return false;
	}

	/**
	 * Returns name of restaurant
	 * 
	 * @return name of restaurant
	 */
	@Override
	public String getName() {
		return name;
	}

	/**
	 * Mutates the name of restaurant
	 * 
	 * @param name
	 *            to be changed to
	 */
	@Override
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Returns state of restaurant
	 * 
	 * @return state
	 */
	@Override
	public String getState() {
		return state;
	}

	/**
	 * Returns city of restaurant
	 * 
	 * @return city
	 */
	@Override
	public String getCity() {
		return city;
	}

	/**
	 * Returns address
	 * 
	 * @return address
	 */
	@Override
	public String getFullAddress() {
		return fullAddress;
	}

	/**
	 * Returns latitude
	 * 
	 * @return latitude
	 */
	@Override
	public double getLatitude() {
		return latitude;
	}

	/**
	 * Returns longitude
	 * 
	 * @return longitude
	 */
	@Override
	public double getLongitude() {
		return longitude;
	}

	/**
	 * Sets the url with the new url
	 * 
	 * @param url
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * Adds category restaurant is in
	 * 
	 * @param category
	 */
	public void addCategory(String category) {
		categories.add(category);
	}

	/**
	 * Removes a category
	 * 
	 * @param category
	 * @return boolean if successful
	 */
	public boolean removeCategory(String category) {
		return categories.remove(category);
	}

	/**
	 * Sets the stars of restaurant
	 * 
	 * @param stars
	 */
	public void setStars(double stars) {
		this.stars = stars;
	}

	/**
	 * Increments review count
	 */
	public void addReview() {
		reviewCount++;
	}

	/**
	 * Decrements review count
	 */
	public void removeReview() {
		reviewCount--;
	}

	/**
	 * Sets photo url with new url
	 * 
	 * @param photoUrl
	 */
	public void setPhotoUrl(String photoUrl) {
		this.photoUrl = photoUrl;
	}

	/**
	 * Sets price with new price
	 * 
	 * @param price
	 */
	public void setPrice(int price) {
		this.price = price;
	}

	// Getters
	/**
	 * Returns if open
	 * 
	 * @return boolean if open
	 */
	public boolean isOpen() {
		return open;
	}

	/**
	 * Gets url
	 * 
	 * @return url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * Returns the neighborhoods restaurant is in
	 * 
	 * @return neighborhood
	 */
	public List<String> getNeighborhoods() {
		List<String> neighborhoodsCopy = new ArrayList<>();
		for (String neighborhood : neighborhoods) {
			neighborhoodsCopy.add(neighborhood);
		}
		return neighborhoodsCopy;
	}

	/**
	 * Returns businessId
	 * 
	 * @return businessId
	 */
	public String getBusinessId() {
		return businessId;
	}

	/**
	 * Gets categories
	 * 
	 * @return categories
	 */
	public List<String> getCategories() {
		List<String> categoriesCopy = new ArrayList<>();
		for (String category : categories) {
			categoriesCopy.add(category);
		}
		return categoriesCopy;
	}

	/**
	 * Get type
	 * 
	 * @return type
	 */
	public String getType() {
		return type;
	}

	/**
	 * Get stars
	 * 
	 * @return stars
	 */
	public double getStars() {
		return stars;
	}

	/**
	 * Gets review count
	 * 
	 * @return reviewCount
	 */
	public int getReviewCount() {
		return reviewCount;
	}

	/**
	 * Gets photoUrl
	 * 
	 * @return photoUrl
	 */
	public String getPhotoUrl() {
		return photoUrl;
	}

	/**
	 * Gets nearby schools
	 * 
	 * @return schools list
	 */
	public List<String> getSchools() {
		List<String> schoolsCopy = new ArrayList<>();
		for (String school : schools) {
			schoolsCopy.add(school);
		}
		return schoolsCopy;
	}

	/**
	 * Gets price
	 * 
	 * @return price
	 */
	public int getPrice() {
		return price;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof YelpRestaurant)) {
			return false;
		}
		YelpRestaurant restaurant = (YelpRestaurant) obj;
		return businessId.equals(restaurant.getBusinessId());
	}
}

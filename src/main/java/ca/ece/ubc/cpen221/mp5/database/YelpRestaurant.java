package ca.ece.ubc.cpen221.mp5.database;

import java.util.ArrayList;
import java.util.List;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;

public class YelpRestaurant implements Business{

    private boolean open;
    private String url;
    private double longitude;
    private List<String> neighborhoods = new ArrayList<>();
    private String businessId;
    private String name;
    private List<String> categories = new ArrayList<>();
    private String state;
    private String type;
    private double stars;					// TODO update stars 
    private String city;
    private String fullAddress;
    private int reviewCount;
    private String photoUrl;
    private List<String> schools = new ArrayList<>();
    private double latitude;
    private int price;

    private List<String> reviews;		// TODO change YelpReview to review ids? 

    // TODO lmao no way this is right 
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
	    	this.neighborhoods.add(neighborhoodJson.get(index).toString());
	    }
	}

	JsonArray schoolsJson = obj.getJsonArray("schools");
	if (schoolsJson != null) {
	    for (int index = 0; index < schoolsJson.size(); index++) {
	    	this.schools.add(schoolsJson.get(index).toString());
	    }
	}

	JsonArray categoriesJson = obj.getJsonArray("categories");
	if (categoriesJson != null) {
	    for (int index = 0; index < categoriesJson.size(); index++) {
	    	this.categories.add(categoriesJson.get(index).toString());
	    }
	}

	reviews = new ArrayList<>();
    }

    // returns a JSON string representing the restaurant 
    public String getJsonString() {
	JsonArrayBuilder neighborhoodsJsonBuilder = Json.createArrayBuilder();
	for (String neighborhood : neighborhoods) {
	    neighborhoodsJsonBuilder.add(neighborhood);
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
	
	JsonObject restaurantJson = Json.createObjectBuilder()
		.add("open", this.open) //TODO: Confirm x is longitude
		.add("url", this.url)
		.add("longitude", this.longitude)
		.add("neighborhoods", neighborhoodsJson)//.toString())
		.add("business_id", this.businessId) //TODO: What is cluster weight?
		.add("name", this.name) 
		.add("categories", categoriesJson.toString()) 
		.add("state", this.state) 
		.add("type", this.type) 
		.add("city", this.city)
		.add("full_address", this.fullAddress)
		.add("review_count", this.reviewCount)
		.add("photo_url", this.photoUrl)
		.add("schools", schoolsJson.toString())
		.add("latitude", this.latitude)
		.add("price", this.price)
		.build();
	
	return restaurantJson.toString();
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
    public String getName() {
	return name;
    }

    @Override
    public void setName(String name) {
	this.name = name;
    }

    @Override
    public String getState() {
	return state;
    }

    @Override
    public String getCity() {
	return city;
    }

    @Override
    public String getFullAddress() {
	return fullAddress;
    }

    @Override
    public double getLatitude() {
	return latitude;
    }

    @Override
    public double getLongitude() {
	return longitude;
    }




    // modifiers
    public void setUrl(String url) {
	this.url = url;
    }

    public void addCategory(String category) {
	categories.add(category);
    }

    public boolean removeCategory(String category) {
	return categories.remove(category);
    }

    public void setStars(int stars) {
	this.stars = stars;
    }

    public void addReview() {
	reviewCount++;
    }

    public void removeReview() {
	reviewCount--;
    }

    public void setPhotoUrl(String photoUrl) {
	this.photoUrl = photoUrl;
    }

    public void setPrice (int price) {
	this.price = price;
    }




    // Getters
    public boolean isOpen() {
	return open;
    }

    public String getUrl() {
	return url;
    }

    public List<String> getNeighborhoods() {
	List<String> neighborhoodsCopy = new ArrayList<>();
	for (String neighborhood : neighborhoods) {
	    neighborhoodsCopy.add(neighborhood);
	}
	return neighborhoodsCopy;
    }

    public String getBusinessId() {
	return businessId;
    }

    public List<String> getCategories() {
	List<String> categoriesCopy = new ArrayList<>();
	for (String category : categories) {
	    categoriesCopy.add(category);
	}
	return categoriesCopy;    
    }

    public String getType() {
	return type;
    }

    public double getStars() {
	return stars;
    }

    public int getReviewCount() {
	return reviewCount;
    }

    public String getPhotoUrl() {
	return photoUrl;
    }

    public List<String> getSchools() {
	List<String> schoolsCopy = new ArrayList<>();
	for (String school : schools) {
	    schoolsCopy.add(school);
	}
	return schoolsCopy;    
    }

    public int getRating() {
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

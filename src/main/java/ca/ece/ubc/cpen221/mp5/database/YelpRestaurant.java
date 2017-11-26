package ca.ece.ubc.cpen221.mp5.database;

import java.util.List;

public class YelpRestaurant implements Business{

    private boolean open;
    private String url;
    private double longitude;
    private List<String> neighborhoods;
    private String businessId;
    private String name;
    private List<String> categories;
    private String state;
    private String type;
    private int stars;
    private String city;
    private String fullAddress;
    private int reviewCount;
    private String photoUrl;
    private List<String> schools;
    private double latitude;
    private int price;
    
    public YelpRestaurant() {
    }

    // TODO lmao no way this is right 
    public YelpRestaurant(boolean open, String url, double longitude, List<String> neighborhoods, String businessId,
	    String name, List<String> categories, String state, String type, int stars, String city,
	    String fullAddress, int reviewCount, String photoUrl, List<String> schools, double latitude, int price) {
	this.open = open;
	this.url = url;
	this.longitude = longitude;
	this.neighborhoods = neighborhoods;
	this.businessId = businessId;
	this.name = name;
	this.categories = categories;
	this.state = state;
	this.type = type;
	this.stars = stars;
	this.city = city;
	this.fullAddress = fullAddress;
	this.reviewCount = reviewCount;
	this.photoUrl = photoUrl;
	this.schools = schools;
	this.latitude = latitude;
	this.price = price;
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
        return neighborhoods;
    }

    public String getBusinessId() {
        return businessId;
    }

    public List<String> getCategories() {
        return categories;
    }

    public String getType() {
        return type;
    }

    public int getStars() {
        return stars;
    }

    public int getReviewCount() {
        return reviewCount;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public List<String> getSchools() {
        return schools;
    }

    public int getPrice() {
        return price;
    }


}

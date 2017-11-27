package ca.ece.ubc.cpen221.mp5.database;

import java.util.ArrayList;
import java.util.List;

import javax.json.JsonObject;

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
    private int stars;					// TODO update stars 
    private String city;
    private String fullAddress;
    private int reviewCount;
    private String photoUrl;
    private List<String> schools;
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
	this.stars = obj.getInt("stars");
	this.city = obj.getString("city");
	this.fullAddress = obj.getString("full_address");
	this.reviewCount = obj.getInt("review_count");
	this.photoUrl = obj.getString("photo_url");
	this.latitude = Double.parseDouble(obj.get("latitude").toString());
	this.price = obj.getInt("price");


	this.neighborhoods = new ArrayList<>();
	for (String neighborhood : neighborhoods) {
	    this.neighborhoods.add(neighborhood);
	}

	this.categories = new ArrayList<>();
	for (String category : categories) {
	    this.categories.add(category);
	}	

	this.schools= new ArrayList<>();
	for (String school : schools) {
	    this.schools.add(school);
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
	List<String> schoolsCopy = new ArrayList<>();
	for (String school : schools) {
	    schoolsCopy.add(school);
	}
	return schoolsCopy;    
    }

    public int getPrice() {
	return price;
    }


}

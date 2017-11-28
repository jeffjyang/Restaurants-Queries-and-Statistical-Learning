package ca.ece.ubc.cpen221.mp5.database;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.*;
import java.util.function.ToDoubleBiFunction;
import java.util.stream.DoubleStream;
import java.util.stream.Stream;

import javax.json.*;

import ca.ece.ubc.cpen221.mp5.MP5Db;

public class YelpDatabase implements MP5Db<YelpRestaurant> {
    private Set<YelpRestaurant> restaurants;
    private Set<YelpUser> users;
    private Set<YelpReview> reviews;

    public YelpDatabase(String restaurantFileName, String reviewFileName, String userFileName) {
	this.restaurants = Collections.synchronizedSet(new HashSet<>());
	this.users = Collections.synchronizedSet(new HashSet<>());
	this.reviews = Collections.synchronizedSet(new HashSet<>());

	parseJsonRestaurant(restaurantFileName);
	parseJsonReviews(reviewFileName);
	parseJsonUsers(userFileName);

    }

    public Set<YelpRestaurant> getRestaurants() {
	return restaurants;
    }

    @Override
    public Set<YelpRestaurant> getMatches(String queryString) {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public ToDoubleBiFunction<MP5Db<YelpRestaurant>, String> getPredictorFunction(String user) {
	YelpUser userObj = getUser(user);
	List<String> userReviewsString = userObj.getReviews();
	List<YelpReview> userReviews = getUserReviews(userReviewsString);

	List<YelpRestaurant> userRestaurants = getUserRestaurants(userReviews);

	double meanPrice = getMeanPrice(userRestaurants);
	double meanStar = getMeanStar(userReviews);
	
	
	List<Double> distFromMeanPrices = distFromMeanPrice(userRestaurants, meanPrice);
	List<Double> distFromMeanStars = distFromMeanStar(userReviews, meanStar);

	double Sxx = sumSquaresOfList(distFromMeanPrices);
	double Syy = sumSquaresOfList(distFromMeanStars);

	double Sxy = sumProductsOfLists(distFromMeanPrices, distFromMeanStars);

	double b = Sxy / Sxx;
	double a = meanStar - b * meanPrice;
	// we dont do anything with rSquared
	//double rSquared = Math.pow(Sxy, 2) / (Sxx * Syy);

	// TODO lmao no way this is right
	ToDoubleBiFunction<MP5Db<YelpRestaurant>, String> fn = (x,y) -> b + a * x.getMatches(y).iterator().next().getPrice(); 
	return fn;
    }



    private double getMeanStar(List<YelpReview> userReviews) {
	double sum = 0;

	for (YelpReview review : userReviews) {
	    sum += review.getStars();
	}
	return sum / userReviews.size();
    }


    private List<Double> distFromMeanStar(List<YelpReview> userReviews, double meanStar) {

	// calculate distance from mean for each element 
	List<Double> distFromMeanStars = new ArrayList<>();
	for (YelpReview review : userReviews) {
	    double dist = review.getStars() - meanStar;
	    distFromMeanStars.add(dist);
	}    
	return distFromMeanStars;
    }

    private double getMeanPrice(List<YelpRestaurant> userRestaurants) {
	double sum = 0;

	for (YelpRestaurant restaurant : userRestaurants) {
	    sum += restaurant.getPrice();
	}
	return sum / userRestaurants.size();
    }


    private List<Double> distFromMeanPrice(List<YelpRestaurant> userRestaurants, double meanPrice) {


	// calculate distance from mean for each element 
	List<Double> distFromMeanPrices = new ArrayList<>();
	for (YelpRestaurant restaurant: userRestaurants) {
	    double dist = restaurant.getPrice() - meanPrice;
	    distFromMeanPrices.add(dist);
	}
	return distFromMeanPrices;
    }



    private List<YelpRestaurant> getUserRestaurants(List<YelpReview> userReviews) {
	List<YelpRestaurant> userRestaurants = new ArrayList<>();

	for (YelpReview review: userReviews) {
	    String restaurantString = review.getBusinessId();
	    userRestaurants.add(getRestaurant(restaurantString));
	}

	return userRestaurants;
    }

    //Converts the user review string list to list of review objects
    private List<YelpReview> getUserReviews(List<String> reviewStrings) {
	List<YelpReview> reviewObjects = new ArrayList<>();

	for (String reviewString : reviewStrings) {
	    reviewObjects.add(getReview(reviewString));
	}

	return reviewObjects;
    }




    private double sumSquaresOfList(List<Double> list) {
	//Using streams to convert

	// sum distance from means to get Sxx
	double sumOfSquares = list.stream()
		.map(s -> Math.pow(s, 2.0))
		.reduce(0.0, Double::sum);

	return sumOfSquares;
    }


    // TODO use streams here 
    private double sumProductsOfLists(List<Double> list1, List<Double> list2) {
	int sum = 0;
	for (int i = 0; i < list1.size(); i ++) {
	    sum += list1.get(i) * list2.get(i);
	}
	return sum;
    }

    private YelpUser getUser(String userID) {
	for (YelpUser user: users) {
	    if (userID.equals(user.getUserId())) {
		return user;
	    }
	}
	return null;	// TODO better way of dealing with this 
    }

    //Returns the review object from the review ID
    private YelpReview getReview(String reviewID) {
	for (YelpReview review: reviews) {
	    if (reviewID.equals(review.getReviewId())) {
		return review;
	    }
	}
	return null; 	// TODO diddo getUser return null
    }

    // TODO make this private???????
    public YelpRestaurant getRestaurant(String businessID) {
	for (YelpRestaurant restaurant: restaurants) {
	    if (businessID.equals(restaurant.getBusinessId())) {
		return restaurant;
	    }
	}
	return null; 		// TODO see above
    }

    @Override
    public String kMeansClusters_json(int k) {
	List<Set<YelpRestaurant>> clusterList = new ArrayList<>();
	ArrayList<Coordinate> seeds = initializeSeeds(k);
	String clusterJson;
	boolean constant = false;

	// Initializing all the cluster sets
	for (int index = 0; index < k; index++) {
	    Set<YelpRestaurant> cluster = new HashSet<>();
	    clusterList.add(cluster);
	}

	// Adding the restaurant to the cluster listing
	for (YelpRestaurant restaurant : restaurants) {
	    int bestSeed = bestCluster(restaurant, seeds);
	    clusterList.get(bestSeed).add(restaurant);
	}

	List<Set<YelpRestaurant>> oldClusterList = new ArrayList<>();

	//for (int index = 0; index < clusterList.size(); index++) { //TODO: Replace with exit loop when constant is asserted 
	//    seeds = centroidUpdate(clusterList, seeds); //Updating the seedList with new centroids
	//    clusterList = clusterUpdate(clusterList, seeds); //Updating the clusterList with the new centroids
	//}

	while (!oldClusterList.equals(clusterList)) {
	    oldClusterList = clusterList;
	    seeds = centroidUpdate(clusterList); //Updating the seedList with new centroids
	    clusterList = clusterUpdate(clusterList, seeds); //Updating the clusterList with the new centroids

	}    
	    
	clusterJson = getJsonString(clusterList);

	return clusterJson;
    }

    private String getJsonString(List<Set<YelpRestaurant>> clusterList) {
	Set<JsonObject> set = new HashSet<>(); 
	JsonArrayBuilder builder = Json.createArrayBuilder();

	for (int index = 0; index < clusterList.size(); index++) {
	    Set<YelpRestaurant> cluster = clusterList.get(index);

	    for (YelpRestaurant restaurant: cluster) {
		JsonObject clusterJson = Json.createObjectBuilder()
			.add("x", restaurant.getLongitude()) //TODO: Confirm x is longitude
			.add("y", restaurant.getLongitude())
			.add("name", restaurant.getName())
			.add("cluster", index)
			.add("weight", 1.0) //TODO: What is cluster weight?
			.build();
		set.add(clusterJson);
	    }
	}

	for (JsonObject jsonObj: set) {
	    builder.add(jsonObj);
    }

    JsonArray jsonArray = builder.build();
	return jsonArray.toString();
    }

    private List<Set<YelpRestaurant>> clusterUpdate(List<Set<YelpRestaurant>> clusterList, ArrayList<Coordinate> seeds) {
	List<Set<YelpRestaurant>> newClusterList = new ArrayList<>();

	for (int index = 0; index < clusterList.size(); index++) {
	    Set<YelpRestaurant> cluster = new HashSet<>();
	    newClusterList.add(cluster);
	}

	for (YelpRestaurant restaurant : restaurants) {
	    int bestCentroid = bestCluster(restaurant, seeds);
	    newClusterList.get(bestCentroid).add(restaurant);
	}

	return newClusterList;
    }

    //Calculates the mean of all data points assigned to each cluster and updates accordingly
    private ArrayList<Coordinate> centroidUpdate(List<Set<YelpRestaurant>> clusterList) {
	ArrayList<Coordinate> newSeeds = new ArrayList<>(); //Copy safety

	for (int index = 0; index < clusterList.size(); index++) {
	    Set<YelpRestaurant> cluster = clusterList.get(index);
	    Coordinate newCentroid = calculateCentroid(cluster);
	    newSeeds.add(newCentroid);
	}

	return newSeeds;
    }

    //Calculates centroids for each cluster
    private Coordinate calculateCentroid(Set<YelpRestaurant> cluster) {
	double sumLat = 0;
	double sumLong = 0;
	double centroidLat;
	double centroidLong;

	for (YelpRestaurant restaurant: cluster) {
	    sumLat += restaurant.getLatitude();
	    sumLong += restaurant.getLongitude();
	}

	centroidLat = sumLat / cluster.size();
	centroidLong = sumLong / cluster.size();

	Coordinate newCentroid = new Coordinate(centroidLat, centroidLong);

	return newCentroid;
    }

    private int bestCluster(YelpRestaurant restaurant, ArrayList<Coordinate> seeds) {
	Coordinate restaurantCoord = new Coordinate(restaurant);
	double minDist = Integer.MAX_VALUE; // Should be something else...
	int bestSeed = -1; // TODO: Add check point for best seed not found

	for (int index = 0; index < seeds.size(); index++) {
	    Coordinate seedCord = seeds.get(index);
	    double distance = computeDistance(restaurantCoord, seedCord);

	    if (distance < minDist) {
		minDist = distance;
		bestSeed = index;
	    }
	}

	return bestSeed;
    }

    private double computeDistance(Coordinate clusterCoord, Coordinate restaurantCoord) {
	double yDist = clusterCoord.getLat() - restaurantCoord.getLat();
	double xDist = clusterCoord.getLong() - restaurantCoord.getLong();
	double distance = Math.pow((Math.pow(xDist, 2.0) + Math.pow(yDist, 2.0)), 0.5);

	return distance;
    }

    private ArrayList<Coordinate> initializeSeeds(int k) {
	Random randomGenerator = new Random();
	List<YelpRestaurant> restaurantList = new ArrayList<>(restaurants);
	ArrayList<Coordinate> seeds = new ArrayList<>();
	Set<Coordinate> generatedCoords = new HashSet<>();

	for (int index = 0; index < k; index++) {
	    // TODO: Account for possibility of more clusters than restaurants
	    int seedIndex = randomGenerator.nextInt(restaurants.size());
	    Coordinate coord = new Coordinate(restaurantList.get(seedIndex));

	    assert (!generatedCoords.contains(coord)); // Check for same coordinate not used for two seeds
	    generatedCoords.add(coord);	// add this coordinate to the already generated coord set
	    seeds.add(coord);
	}

	return seeds;
    }


    private void parseJsonRestaurant(String jsonDir) {
	InputStream is;

	try {
	    FileReader fr = new FileReader(jsonDir);
	    BufferedReader br = new BufferedReader(fr);
	    String line = br.readLine();

	    while (line != null) {
		is = new ByteArrayInputStream(line.getBytes());
		JsonReader reader = Json.createReader(is);
		JsonObject obj = reader.readObject();

		YelpRestaurant restaurant = new YelpRestaurant(obj);

		this.restaurants.add(restaurant);
		line = br.readLine();
	    }

	    br.close();

	} catch (FileNotFoundException e) {
	    e.printStackTrace();
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }

    private void parseJsonUsers(String jsonDir) {
	InputStream is;

	try {
	    FileReader fr = new FileReader(jsonDir);
	    BufferedReader br = new BufferedReader(fr);
	    String line = br.readLine();

	    while (line != null) {
		is = new ByteArrayInputStream(line.getBytes());
		JsonReader reader = Json.createReader(is);
		JsonObject obj = reader.readObject();

		YelpUser user = new YelpUser(obj);

		this.users.add(user);
		line = br.readLine();
	    }

	    br.close();

	} catch (FileNotFoundException e) {
	    e.printStackTrace();
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }

    private void parseJsonReviews(String jsonDir) {
	InputStream is;

	try {
	    FileReader fr = new FileReader(jsonDir);
	    BufferedReader br = new BufferedReader(fr);
	    String line = br.readLine();

	    while (line != null) {
		is = new ByteArrayInputStream(line.getBytes());
		JsonReader reader = Json.createReader(is);
		JsonObject obj = reader.readObject();

		YelpReview review = new YelpReview(obj);

		this.reviews.add(review);
		line = br.readLine();
	    }

	    br.close();

	} catch (FileNotFoundException e) {
	    e.printStackTrace();
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }

}

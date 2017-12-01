package ca.ece.ubc.cpen221.mp5.database;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.Map.Entry;
import java.util.function.ToDoubleBiFunction;

import javax.json.*;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import ca.ece.ubc.cpen221.mp5.MP5Db;
import ca.ece.ubc.cpen221.mp5.query.QueryLexer;
import ca.ece.ubc.cpen221.mp5.query.QueryListenerFilterCreator;
import ca.ece.ubc.cpen221.mp5.query.QueryParser;

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

    // return null if invalid query 
    // return empty set if no such restaurant 
    @Override
    public Set<YelpRestaurant> getMatches(String queryString) {
	queryString = replaceWhiteSpace(queryString);
	CharStream stream = CharStreams.fromString(queryString);
	QueryLexer lexer = new QueryLexer(stream);
	TokenStream tokens = new CommonTokenStream(lexer);
	QueryParser parser = new QueryParser(tokens);
	ParseTree tree = parser.root();
	ParseTreeWalker walker = new ParseTreeWalker();
	// QueryListener listener = new QueryListenerPrintEverything();
	QueryListenerFilterCreator listener = new QueryListenerFilterCreator(restaurants);
	walker.walk(listener, tree);
	Set<YelpRestaurant> queryRestaurants = listener.getQueryRestaurants();
	//System.out.println(queryRestaurants);
	return queryRestaurants; //Returns null if invalid query
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
	// double rSquared = Math.pow(Sxy, 2) / (Sxx * Syy);

	// TODO lmao no way this is right
	ToDoubleBiFunction<MP5Db<YelpRestaurant>, String> fn = (x, y) -> b
		+ a * x.getMatches(y).iterator().next().getPrice();
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
	for (YelpRestaurant restaurant : userRestaurants) {
	    double dist = restaurant.getPrice() - meanPrice;
	    distFromMeanPrices.add(dist);
	}
	return distFromMeanPrices;
    }

    private List<YelpRestaurant> getUserRestaurants(List<YelpReview> userReviews) {
	List<YelpRestaurant> userRestaurants = new ArrayList<>();

	for (YelpReview review : userReviews) {
	    String restaurantString = review.getBusinessId();
	    userRestaurants.add(getRestaurant(restaurantString));
	}

	return userRestaurants;
    }

    // Converts the user review string list to list of review objects
    private List<YelpReview> getUserReviews(List<String> reviewStrings) {
	List<YelpReview> reviewObjects = new ArrayList<>();

	for (String reviewString : reviewStrings) {
	    reviewObjects.add(getReview(reviewString));
	}

	return reviewObjects;
    }

    private double sumSquaresOfList(List<Double> list) {
	// Using streams to convert

	// sum distance from means to get Sxx
	double sumOfSquares = list.stream().map(s -> Math.pow(s, 2.0)).reduce(0.0, Double::sum);

	return sumOfSquares;
    }

    // TODO use streams here
    private double sumProductsOfLists(List<Double> list1, List<Double> list2) {
	int sum = 0;
	for (int i = 0; i < list1.size(); i++) {
	    sum += list1.get(i) * list2.get(i);
	}
	return sum;
    }

    private YelpUser getUser(String userID) {
	for (YelpUser user : users) {
	    if (userID.equals(user.getUserId())) {
		return user;
	    }
	}
	return null; // TODO better way of dealing with this
    }

    // Returns the review object from the review ID
    private YelpReview getReview(String reviewID) {
	for (YelpReview review : reviews) {
	    if (reviewID.equals(review.getReviewId())) {
		return review;
	    }
	}
	return null; // TODO diddo getUser return null
    }

    // TODO make this private???????
    public YelpRestaurant getRestaurant(String businessID) {
	for (YelpRestaurant restaurant : restaurants) {
	    if (businessID.equals(restaurant.getBusinessId())) {
		return restaurant;
	    }
	}
	return null; // TODO see above
    }

    // TODO return null value or err code of restaurant doesnt exist?????
    public String getRestaurantJson(String businessID) {
	YelpRestaurant restaurant = getRestaurant(businessID);
	if (restaurant == null) {
	    return "";
	}

	return restaurant.getJsonString();
    }

    // return json string if user added successfully, empty string otherwise
    public String adduser(String userJson) {

	InputStream is = new ByteArrayInputStream(userJson.getBytes());
	JsonReader reader = Json.createReader(is);
	JsonObject userInput;
	try {
	    userInput = reader.readObject();
	} catch (Exception e) {
	    System.err.println("bad user input");;
	    return "";
	}

	if (userInput.isNull("name")) {
	    return "";
	}

	String name = userInput.getString("name");
	String url = "google.com/eyylmao this is totally a url";
	String userId = "TotallyARandomStringLel";

	JsonObject reviewCount = Json.createObjectBuilder().add("funny", 0).add("userful", 0).add("cool", 0).build();

	JsonObject newUserJson = Json.createObjectBuilder().add("url", url).add("votes", reviewCount.toString()) // json
		// to
		// string
		.add("review_count", 0).add("type", "user").add("user_id", userId).add("name", name)
		.add("average_stars", 0).build();

	YelpUser newUser = new YelpUser(newUserJson);

	users.add(newUser);

	return newUserJson.toString();

    }

    // TODO add restaurant , return empty string if invalid
    public String addRestaurant(String restaurantJsonString) {
	InputStream is = new ByteArrayInputStream(restaurantJsonString.getBytes());
	JsonReader reader = Json.createReader(is);
	JsonObject restaurantJson;
	try {
	    restaurantJson = reader.readObject();
	} catch (Exception e) {
	    System.err.println("bad user input");;
	    return "";
	}
	// TODO check for error in fields

	String businessId = "VerySecureRandomString";

	JsonObjectBuilder newRestaurantBuilder = Json.createObjectBuilder();

	for (Entry<String, JsonValue> property : restaurantJson.entrySet()) {
	    newRestaurantBuilder.add(property.getKey(), property.getValue());
	}

	newRestaurantBuilder.add("business_id", businessId);
	newRestaurantBuilder.add("stars", 0);
	JsonObject newRestaurantJson = newRestaurantBuilder.build();

	YelpRestaurant newRestaurant = new YelpRestaurant(newRestaurantJson);

	restaurants.add(newRestaurant);

	return newRestaurantJson.toString();

    }

    // TODO error codes
    // return "" if invalid review json string
    // "1" if restaurant doesnt exist
    // "2" if user doesnt exist 
    public String addReview(String reviewJsonString) {
	InputStream is = new ByteArrayInputStream(reviewJsonString.getBytes());
	JsonReader reader = Json.createReader(is);
	JsonObject reviewJson;
	try {
	    reviewJson = reader.readObject();
	} catch (Exception e) {
	    e.printStackTrace();
	    return "";
	}

	// TODO validate json string

	String businessId = reviewJson.getString("business_id");
	String userId = reviewJson.getString("user_id");

	// TODO error codes
	if (this.getRestaurant(businessId) == null) {
	    return "1";
	}
	if (this.getRestaurant(userId) == null) {
	    return "2";
	}

	YelpReview newReview = new YelpReview(reviewJson);

	reviews.add(newReview);

	return reviewJson.toString();

    }

    @Override
    public String kMeansClusters_json(int k) {
	boolean flag = false;
	List<Set<YelpRestaurant>> clusterList;// = new ArrayList<>();
	String clusterJson;
	// ArrayList<Coordinate> seeds = new ArrayList<>();

	do {
	    flag = false;
	    // List<Set<YelpRestaurant>> clusterList = new ArrayList<>();
	    clusterList = new ArrayList<>();
	    ArrayList<Coordinate> seeds = new ArrayList<>();
	    seeds = initializeSeeds(k);
	    // String clusterJson;

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

	    while (!oldClusterList.equals(clusterList)) {
		oldClusterList = clusterList;
		// System.out.println("efwfewf" + clusterList.size());
		seeds = centroidUpdate(clusterList); // Updating the seedList with new centroids
		clusterList = clusterUpdate(clusterList, seeds); // Updating the clusterList with the new centroids
		if (clusterList == null) {
		    flag = true;
		    break;
		}
	    }
	    System.out.println("how many times");
	} while (flag == true);

	clusterJson = getJsonString(clusterList);

	return clusterJson;
    }

    private String getJsonString(List<Set<YelpRestaurant>> clusterList) {
	Set<JsonObject> set = new HashSet<>();
	JsonArrayBuilder builder = Json.createArrayBuilder();

	for (int index = 0; index < clusterList.size(); index++) {
	    Set<YelpRestaurant> cluster = clusterList.get(index);

	    for (YelpRestaurant restaurant : cluster) {
		JsonObject clusterJson = Json.createObjectBuilder().add("x", restaurant.getLatitude()) // TODO: Confirm
			// x is
			// longitude
			.add("y", restaurant.getLongitude()).add("name", restaurant.getName()).add("cluster", index)
			.add("weight", 1.0) // TODO: What is cluster weight?
			.build();
		set.add(clusterJson);
	    }
	}

	for (JsonObject jsonObj : set) {
	    builder.add(jsonObj);
	}

	JsonArray jsonArray = builder.build();
	return jsonArray.toString();

    }

    private List<Set<YelpRestaurant>> clusterUpdate(List<Set<YelpRestaurant>> clusterList,
	    ArrayList<Coordinate> seeds) {
	List<Set<YelpRestaurant>> newClusterList = new ArrayList<>();

	for (int index = 0; index < clusterList.size(); index++) {
	    Set<YelpRestaurant> cluster = new HashSet<>();
	    newClusterList.add(cluster);
	}

	for (YelpRestaurant restaurant : restaurants) {
	    int bestCentroid = bestCluster(restaurant, seeds);
	    newClusterList.get(bestCentroid).add(restaurant);
	}
	// System.out.println("efjklw" + newClusterList.size());
	for (Set<YelpRestaurant> set : newClusterList) {
	    if (set.isEmpty()) {
		return null;
	    }
	}
	return newClusterList;
    }

    // Calculates the mean of all data points assigned to each cluster and updates
    // accordingly
    private ArrayList<Coordinate> centroidUpdate(List<Set<YelpRestaurant>> clusterList) {
	ArrayList<Coordinate> newSeeds = new ArrayList<>(); // Copy safety

	for (int index = 0; index < clusterList.size(); index++) {
	    Set<YelpRestaurant> cluster = clusterList.get(index);
	    Coordinate newCentroid = calculateCentroid(cluster);
	    newSeeds.add(newCentroid);
	}

	return newSeeds;
    }

    // Calculates centroids for each cluster
    private Coordinate calculateCentroid(Set<YelpRestaurant> cluster) {
	double sumLat = 0;
	double sumLong = 0;
	double centroidLat;
	double centroidLong;

	for (YelpRestaurant restaurant : cluster) {
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

	    int seedIndex = randomGenerator.nextInt(restaurantList.size());
	    Coordinate coord = new Coordinate(restaurantList.get(seedIndex));
	    restaurantList.remove(seedIndex);

	    generatedCoords.add(coord); // add this coordinate to the already generated coord set
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

    private static String replaceWhiteSpace(String query) {
	boolean openBracket = false;
	char[] queryArray = query.toCharArray();

	for (int index = 0; index < queryArray.length; index++) {
	    if (queryArray[index] == '(')
		openBracket = true;
	    else if (queryArray[index] == ')')
		openBracket = false;
	    else if (openBracket && queryArray[index] == ' ') {
		queryArray[index] = '_';
	    }
	}
	return String.valueOf(queryArray);
    }

}

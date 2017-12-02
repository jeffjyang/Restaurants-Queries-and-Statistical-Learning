package ca.ece.ubc.cpen221.mp5.database;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.function.ToDoubleBiFunction;

import javax.json.*;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ConsoleErrorListener;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import ca.ece.ubc.cpen221.mp5.MP5Db;
import ca.ece.ubc.cpen221.mp5.query.QueryLexer;
import ca.ece.ubc.cpen221.mp5.query.QueryListenerFilterCreator;
import ca.ece.ubc.cpen221.mp5.query.QueryParser;

/**
 * Rep Invariant: Set of all Restaurant, Review, and User Sets are composed of
 * Restaurant, Review, and User objects Sets are not null Abstraction function:
 * Represents the Yelp database
 * 
 * Abstraction Function: String JSONRestauant -> Set of YelpRestaurant Objects
 * String JSONReviews -> Set of YelpReview Object String JSONUser -> Set of
 * YelpUser Object
 */
public class YelpDatabase implements MP5Db<YelpRestaurant> {
	private Set<YelpRestaurant> restaurants;
	private Set<YelpUser> users;
	private Set<YelpReview> reviews;

	/**
	 * Constructs YelpDatabase from JSON file locations
	 * 
	 * @param restaurantFileName
	 *            Location where restaurant JSON file can be found
	 * @param reviewFileName
	 *            Location where review JSON file can be found
	 * @param userFileName
	 *            Location where user JSON file can be found
	 */
	public YelpDatabase(String restaurantFileName, String reviewFileName, String userFileName) {
		this.restaurants = Collections.synchronizedSet(new HashSet<>());
		this.users = Collections.synchronizedSet(new HashSet<>());
		this.reviews = Collections.synchronizedSet(new HashSet<>());

		parseJsonRestaurant(restaurantFileName);
		parseJsonUsers(userFileName);
		parseJsonReviews(reviewFileName);
	}

	public Set<YelpRestaurant> getRestaurants() {
		return restaurants;
	}

	/**
	 * Outputs all the sets that match the query request Performs lexigraphical
	 * analysis on query and filters lists
	 * 
	 * @param queryString
	 *            query to be matched
	 * @return Set<YelpRestaurant> restaurants that match query request Returns null
	 *         if invalid query Returns empty set if not restaurants match query
	 */
	@Override
	public Set<YelpRestaurant> getMatches(String queryString) {
		Set<YelpRestaurant> queryRestaurants;
		if (!(queryString.contains("rating") || queryString.contains("in(") || queryString.contains("price")
				|| queryString.contains("category(") || queryString.contains("name"))) {

			if (this.containsRestaurant(queryString)) {
				Set<YelpRestaurant> set = new HashSet<>();
				set.add(this.getRestaurant(queryString));
				return set;
			}

			return null;
		}
		queryString = replaceWhiteSpace(queryString); //Replacing white space for underscores
		CharStream stream = CharStreams.fromString(queryString); //ANTLR construction
		QueryLexer lexer = new QueryLexer(stream);
		TokenStream tokens = new CommonTokenStream(lexer);
		QueryParser parser = new QueryParser(tokens);
		parser.removeErrorListener(ConsoleErrorListener.INSTANCE); //Removing ANTLR errors
		lexer.removeErrorListener(ConsoleErrorListener.INSTANCE);
		ParseTree tree = parser.root();
		ParseTreeWalker walker = new ParseTreeWalker();
		// QueryListener listener = new QueryListenerPrintEverything(); For testing purposes
		QueryListenerFilterCreator listener = new QueryListenerFilterCreator(restaurants); //Creates new listener
		walker.walk(listener, tree);
		queryRestaurants = listener.getQueryRestaurants();

		return queryRestaurants; // Returns null if invalid query
	}

	/**
	 * Performs k-mean clustering on the Restaurant list based on distance
	 * 
	 * @param int
	 *            k indicating how many clusters to be made
	 * @return JSONString of list of clusters
	 */

	@Override
	public String kMeansClusters_json(int k) {
		boolean flag = false; // Flag to indicate if cluster contains empty clusters
		List<Set<YelpRestaurant>> clusterList;
		String clusterJson;

		do {
			flag = false;
			clusterList = new ArrayList<>();
			ArrayList<Coordinate> seeds = new ArrayList<>();
			seeds = initializeSeeds(k);

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
				seeds = centroidUpdate(clusterList); // Updating the seedList with new centroids
				clusterList = clusterUpdate(clusterList, seeds); // Updating the clusterList with the new centroids
				if (clusterList == null) {
					flag = true;
					break;
				}
			}
		} while (flag == true);

		clusterJson = getJsonString(clusterList);

		return clusterJson;
	}

	/**
	 * Performs a simple least-squares linear regression on the reviews a user has
	 * written. Returns a ToDoubleBiFunction that can be used to predict the user's
	 * rating for a particular restaurant
	 * 
	 * @param user
	 *            the user_id of the user who we wish to predict their ratings
	 *            Requires: the user exists in the database
	 * @return returns a function that can be used to predict the user's ratings
	 */
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
		// we don't do anything with rSquared
		// double rSquared = Math.pow(Sxy, 2) / (Sxx * Syy);

		ToDoubleBiFunction<MP5Db<YelpRestaurant>, String> fn = (x, y) -> b
				+ a * x.getMatches(y).iterator().next().getPrice();

		return fn;
	}

	// helper method for getPredictorFunction
	private double getMeanStar(List<YelpReview> userReviews) {
		double sum = 0;

		for (YelpReview review : userReviews) {
			sum += review.getStars();
		}
		return sum / userReviews.size();
	}

	// helper method for getPredictorFunction
	private List<Double> distFromMeanStar(List<YelpReview> userReviews, double meanStar) {
		// calculate distance from mean for each element
		List<Double> distFromMeanStars = new ArrayList<>();
		for (YelpReview review : userReviews) {
			double dist = review.getStars() - meanStar;
			distFromMeanStars.add(dist);
		}
		return distFromMeanStars;
	}

	// helper method for getPredictorFunction
	private double getMeanPrice(List<YelpRestaurant> userRestaurants) {
		double sum = 0;
		for (YelpRestaurant restaurant : userRestaurants) {
			sum += restaurant.getPrice();
		}
		return sum / userRestaurants.size();
	}

	// helper method for getPredictorFunction
	private List<Double> distFromMeanPrice(List<YelpRestaurant> userRestaurants, double meanPrice) {
		// calculate distance from mean for each element
		List<Double> distFromMeanPrices = new ArrayList<>();
		for (YelpRestaurant restaurant : userRestaurants) {
			double dist = restaurant.getPrice() - meanPrice;
			distFromMeanPrices.add(dist);
		}
		return distFromMeanPrices;
	}

	// helper method for getPredictorFunction
	private List<YelpRestaurant> getUserRestaurants(List<YelpReview> userReviews) {
		List<YelpRestaurant> userRestaurants = new ArrayList<>();
		for (YelpReview review : userReviews) {
			String restaurantString = review.getBusinessId();
			userRestaurants.add(getRestaurant(restaurantString));
		}
		return userRestaurants;
	}

	// helper method for getPredictorFunction
	// Converts the user review string list to list of review objects
	private List<YelpReview> getUserReviews(List<String> reviewStrings) {
		List<YelpReview> reviewObjects = new ArrayList<>();
		for (String reviewString : reviewStrings) {
			reviewObjects.add(getReview(reviewString));
		}
		return reviewObjects;
	}

	// helper method for getPredictorFunction
	private double sumSquaresOfList(List<Double> list) {
		// sum distance from means to get Sxx
		double sumOfSquares = list.stream().map(s -> Math.pow(s, 2.0)).reduce(0.0, Double::sum);
		return sumOfSquares;
	}

	// helper method for getPredictorFunction
	private double sumProductsOfLists(List<Double> list1, List<Double> list2) {
		int sum = 0;
		for (int i = 0; i < list1.size(); i++) {
			sum += list1.get(i) * list2.get(i);
			System.out.println("times tables " + list1.get(i) * list2.get(i));
		}
		return sum;
	}

	/**
	 * Returns the YelpUser associated with the userID. Returns null if no such user
	 * 
	 * @param userID
	 *            the user_id of the YelpUser
	 * @return the YelpUser associated with the userID, null if no such YelpUser
	 *         exists
	 */
	public YelpUser getUser(String userID) {
		for (YelpUser user : users) {
			if (userID.equals(user.getUserId())) {
				return user;
			}
		}
		return null; // TODO better way of dealing with this
	}

	/**
	 * Returns the YelpReview associated with the reviewID. Returns null if no such
	 * user
	 * 
	 * @param reviewID
	 *            the review_id of the YelpReview
	 * @return the YelpReview associated with the reviewID, null if no such
	 *         YelpReview exists
	 */
	public YelpReview getReview(String reviewID) {
		for (YelpReview review : reviews) {
			if (reviewID.equals(review.getReviewId())) {
				return review;
			}
		}
		return null;
	}

	/**
	 * Returns the YelpRestaurant associated with the businessID. Returns null if no
	 * such user
	 * 
	 * @param businessID
	 *            the business_id of the YelpRestaurant
	 * @return the YelpRestaurant associated with the reviewID, null if no such
	 *         YelpRestaurant exists
	 */
	public YelpRestaurant getRestaurant(String businessID) {
		for (YelpRestaurant restaurant : restaurants) {
			if (businessID.equals(restaurant.getBusinessId())) {
				return restaurant;
			}
		}
		return null;
	}

	/**
	 * Adds a YelpUser to the database
	 * 
	 * @param user
	 *            the YelpUser we want to add to the database
	 */
	public void addUser(YelpUser user) {
		users.add(user);
	}

	/**
	 * Adds a YelpRestaurant to the database
	 * 
	 * @param restaurant
	 *            the YelpRestaurant we want to add to the database
	 */
	public void addRestaurant(YelpRestaurant restaurant) {
		restaurants.add(restaurant);
	}

	/**
	 * Checks to see if the YelpRestaurant associated with the businessId exists in
	 * the database
	 * 
	 * @param businessId
	 *            the business_id of the YelpRestaurant we want to see if it exists
	 *            in the database
	 * @return true if such a YelpRestaurant exists in the database, false otherwise
	 */
	public boolean containsRestaurant(String businessId) {
		for (YelpRestaurant restaurant : restaurants) {
			if (businessId.equals(restaurant.getBusinessId())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Checks to see if the YelpUser associated with the userId exists in the
	 * database
	 * 
	 * @param userId
	 *            the user_id of the YelpRestaurant we want to see if it exists in
	 *            the database
	 * @return true if such a YelpUser exists in the database, false otherwise
	 */
	public boolean containsUser(String userId) {
		for (YelpUser user : users) {
			if (userId.equals(user.getUserId())) {
				return true;
			}
		}
		return false;
	}

	// requres review to be prevalidated
	public void addReview(YelpReview review) {

		// update restaurant fields
		String businessId = review.getBusinessId();
		YelpRestaurant toModifyRestaurant = null;
		for (YelpRestaurant restaurant : restaurants) {
			if (businessId.equals(restaurant.getBusinessId())) {
				toModifyRestaurant = restaurant;
				break;
			}
		}
		double newRestaurantStars = (toModifyRestaurant.getStars() * toModifyRestaurant.getReviewCount()
				+ review.getStars()) / (toModifyRestaurant.getReviewCount() + 1);
		toModifyRestaurant.addReview(review.getReviewId());
		toModifyRestaurant.setStars(newRestaurantStars);

		String userId = review.getUserId();
		YelpUser toModifyUser = null;
		for (YelpUser user : users) {
			if (userId.equals(user.getUserId())) {
				toModifyUser = user;
				break;
			}
		}

		// update user fields
		double newUserStars = (toModifyUser.getAverageStars() * toModifyUser.getReviewCount() + review.getStars())
				/ (toModifyUser.getReviewCount() + 1);
		toModifyUser.addReview(review.getReviewId());
		toModifyUser.setAverageStars(newUserStars);

		Map<String, Integer> userVotes = toModifyUser.getVotesMutable();

		Map<String, Integer> reviewVotes = review.getVotes();
		for (String category : reviewVotes.keySet()) {
			if (userVotes.containsKey(category)) {
				userVotes.put(category, userVotes.get(category) + reviewVotes.get(category));
			}
		}

		reviews.add(review);

	}
	
	/**
	 * Returns a String containing an JSONArray of the cluster
	 * 
	 * @param clusterList list of all cluster sets
	 * @return JSONString to be output
	 */
	private String getJsonString(List<Set<YelpRestaurant>> clusterList) {
		Set<JsonObject> set = new HashSet<>();
		JsonArrayBuilder builder = Json.createArrayBuilder();

		for (int index = 0; index < clusterList.size(); index++) {
			Set<YelpRestaurant> cluster = clusterList.get(index);

			for (YelpRestaurant restaurant : cluster) { //Assuming x is latitude and y is longitude
				JsonObject clusterJson = Json.createObjectBuilder().add("x", restaurant.getLatitude()) 
						.add("y", restaurant.getLongitude()).add("name", restaurant.getName()).add("cluster", index)
						.add("weight", 1.0) 
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

	/**
	 * Updates the cluster list by averaging the cluster node coordinates
	 * 
	 * @param clusterList
	 *            List of cluster sets containing restaurants
	 * @param seeds
	 *            Coordinates of the current seeds
	 * @return new clusterList using the updated averaged centroids
	 */
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
		for (Set<YelpRestaurant> set : newClusterList) {
			if (set.isEmpty()) {
				return null;
			}
		}
		return newClusterList;
	}

	/**
	 * Updates the centroid list by averaging out all data point coordinates for
	 * each individual cluster
	 * 
	 * @param clusterList
	 *            List containing all sets of YelpRestaurants
	 * @return newClusterList containing the updated centroid list coordinates
	 */
	private ArrayList<Coordinate> centroidUpdate(List<Set<YelpRestaurant>> clusterList) {
		ArrayList<Coordinate> newSeeds = new ArrayList<>(); // Copy safety

		for (int index = 0; index < clusterList.size(); index++) {
			Set<YelpRestaurant> cluster = clusterList.get(index);
			Coordinate newCentroid = calculateCentroid(cluster);
			newSeeds.add(newCentroid);
		}

		return newSeeds;
	}

	/**
	 * Calculates the coordinates of the averaged out cluster data point
	 * 
	 * @param cluster
	 *            Set containing all YelpRestaurants in the cluster
	 * @return Coordinate of the averaged out cluster data
	 */
	private Coordinate calculateCentroid(Set<YelpRestaurant> cluster) {
		double sumLat = 0;
		double sumLong = 0;
		double centroidLat;
		double centroidLong;

		for (YelpRestaurant restaurant : cluster) {
			sumLat += restaurant.getLatitude();
			sumLong += restaurant.getLongitude();
		}

		centroidLat = sumLat / cluster.size(); //Averaging the clusters
		centroidLong = sumLong / cluster.size();

		Coordinate newCentroid = new Coordinate(centroidLat, centroidLong);

		return newCentroid;
	}

	/**
	 * Returns the integer corresponding to the best cluster from the given
	 * restaurant
	 * 
	 * @param restaurant
	 *            to compare clusters with
	 * @param seeds
	 *            List of Coordinates of the clusters
	 * @return Integer corresponding to best cluster for the restaurant
	 */
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

	/**
	 * Returns the euclidean distance between two coordinates
	 * 
	 * @param clusterCoord
	 *            Coordinate off the cluster centroid
	 * @param restaurantCoord
	 *            Coordinate of the restaurant to be compared with
	 * @return distance between the two coordinates
	 */
	private double computeDistance(Coordinate clusterCoord, Coordinate restaurantCoord) {
		double yDist = clusterCoord.getLat() - restaurantCoord.getLat();
		double xDist = clusterCoord.getLong() - restaurantCoord.getLong();
		double distance = Math.pow((Math.pow(xDist, 2.0) + Math.pow(yDist, 2.0)), 0.5);

		return distance;
	}

	/**
	 * Randomizes seed locations by pulling random centroid locations from the
	 * restaurantList Outputs k number of seeds
	 * 
	 * @param k
	 *            number of seeds to be generated
	 * @return ArrayList<Coordinate> corresponding to the randomized seed locations
	 */
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

	/**
	 * Parses the JSON value for the restaurants and constructs new YelpRestaurant
	 * for each restaurant within the JSON array
	 * 
	 * @param jsonDir
	 *            location where JSON data can be found
	 */
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

	/**
	 * Parses the JSON value for the users and constructs new YelpUser for each user
	 * within the JSON array
	 * 
	 * @param jsonDir
	 *            location where JSON data can be found
	 */
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

	/**
	 * Parses the JSON value for the reviews and constructs new YelpReview for each
	 * review within the JSON array
	 * 
	 * @param jsonDir
	 *            location where JSON data can be found
	 */
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

				String reviewUserId = review.getUserId();

				for (YelpUser user : users) {
					if (user.getUserId().equals(reviewUserId)) {
						user.addReviewInit(review.getReviewId());
						break;
					}
				}

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

	/**
	 * Replaces the whitespace within brackets with underscore
	 * 
	 * @param query
	 *            data to replace whitespace for
	 * @return String containing query with spaces replaced for underscores
	 */
	private static String replaceWhiteSpace(String query) {
		boolean openBracket = false;
		boolean adjacentAlphabet = false;
		char[] queryArray = query.toCharArray();

		for (int index = 0; index < queryArray.length; index++) {
			adjacentAlphabet = adjacentAreAlpha(queryArray, index);

			if (queryArray[index] == '(')
				openBracket = true;
			else if (queryArray[index] == ')')
				openBracket = false;
			else if (openBracket && queryArray[index] == ' ' && adjacentAlphabet) {
				queryArray[index] = '_';
			}
			adjacentAlphabet = false;
		}
		return String.valueOf(queryArray);
	}

	/**
	 * Checks if the character on left and right are alphabets
	 * 
	 * @param queryArray
	 *            character array to be tested
	 * @param index
	 *            character index
	 * @return true if they are both alphabets false otherwise
	 */
	private static boolean adjacentAreAlpha(char[] queryArray, int index) {
		if (index >= 1 && index < queryArray.length - 1) {
			int leftChar = queryArray[index - 1];
			int rightChar = queryArray[index + 1];

			if (leftChar >= 65 && leftChar <= 122 && rightChar >= 65 && leftChar <= 122) { //Tests left and right for ascii
				return true;
			}
		}
		return false;
	}
}

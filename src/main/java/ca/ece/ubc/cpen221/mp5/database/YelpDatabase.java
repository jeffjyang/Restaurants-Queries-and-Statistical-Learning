package ca.ece.ubc.cpen221.mp5.database;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.function.ToDoubleBiFunction;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonWriter;

import ca.ece.ubc.cpen221.mp5.MP5Db;

public class YelpDatabase implements MP5Db<Review> {
	private Set<YelpRestaurant> restaurants;
	private Set<User> users;
	private Set<Review> reviews;

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
	public Set<Review> getMatches(String queryString) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ToDoubleBiFunction<MP5Db<Review>, String> getPredictorFunction(String user) {
		// TODO Auto-generated method stub
		return null;
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
		
		for (int index = 0; index < clusterList.size(); index++) { //TODO: Replace with exit loop when constant is asserted 
			seeds = centroidUpdate(clusterList, seeds); //Updating the seedList with new centroids
			clusterList = clusterUpdate(clusterList, seeds); //Updating the clusterList with the new centroids
		}
		
		clusterJson = getJsonString(clusterList);

		return clusterJson;
	}
	
	private String getJsonString(List<Set<YelpRestaurant>> clusterList) {
		String jsonString = "";
		Set<JsonObject> set = new HashSet<>(); 
		JsonArray jsonArray = Json.createArrayBuilder().build();
		
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
		
		jsonArray.addAll(set);
		
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
	private ArrayList<Coordinate> centroidUpdate(List<Set<YelpRestaurant>> clusterList, ArrayList<Coordinate> seeds) {
		ArrayList<Coordinate> newSeeds = new ArrayList<>(seeds); //Copy safety
		
		for (int index = 0; index < clusterList.size(); index++) {
			Set<YelpRestaurant> cluster = clusterList.get(index);
			Coordinate newCentroid = calculateCentroid(cluster);
			newSeeds.add(index, newCentroid);
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

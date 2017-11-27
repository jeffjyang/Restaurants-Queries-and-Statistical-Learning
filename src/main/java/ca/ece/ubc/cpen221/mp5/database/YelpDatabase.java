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
import java.util.stream.Stream;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonWriter;

import ca.ece.ubc.cpen221.mp5.MP5Db;

public class YelpDatabase implements MP5Db<YelpUser> {
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
	public Set<YelpUser> getMatches(String queryString) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ToDoubleBiFunction<MP5Db<YelpUser>, String> getPredictorFunction(String user) {
		YelpUser userObj = getUser(user);
		List<String> userReviewString = userObj.getReviews();
		List<YelpReview> userReviews = convertToReviews(userReviewString);
		List<YelpRestaurant> userRestaurants = getUserRestaurants(userReviews);
        List<Double> xMinMean = calcXMinMean()
        List<Double> yMinMean = new ArrayList<>();



		double Sxx = calculateSxx(); //x indicated by restaurant priciness
		double Syy = calculateS(); //y indicated by user's rating


		return null;
	}

    //Converts the user review string list to list of review objects
	private List<YelpReview> convertToReviews(List<String> reviewStrings) {
        List<YelpReview> convertedReviews = new ArrayList<>();

        for (String reviewString : reviewStrings) {
	        convertedReviews.add(getReview(reviewString));
        }

        return convertedReviews;
    }

    private List<Double> calcXMinMean(List<YelpRestaurant> userRestaurants) {
	    double pricinessMean = calcPricinessMean(userRestaurants);
        List<Double> xMinMean = new ArrayList<>();


	    for (YelpRestaurant restaurant: userRestaurants) {
	        double restaurantXMinMean = restaurant.getPrice() - pricinessMean;
	        xMinMean.add(restaurantXMinMean);
        }

	    return xMinMean;
    }

    private List<YelpRestaurant> getUserRestaurants(List<YelpReview> userReviews) {
	    List<YelpRestaurant> userRestaurants = new ArrayList<>();

	    for (YelpReview review: userReviews) {
	        String restaurantString = review.getBusinessId();
	        userRestaurants.add(getRestaurant(restaurantString));
        }

        return userRestaurants;
    }

    private double calculateSxx(List<Double> xMinMean) {
       //Using streams to convert
        double Sxx = xMinMean.stream()
                .map(s -> Math.pow(s, 2.0))
                .reduce(0.0, Double::sum);

        return Sxx;
    }

/*
	private double calculateSyy(YelpUser initial) {
		double priceMean = calcPricinessMean();
		double sumSquare = 0;

		for (YelpRestaurant restaurant: restaurants) {
			int xPrice = restaurant.getPrice();
			double difference = xPrice - priceMean;
			sumSquare += Math.pow(difference, 2.0);
		}

		return sumSquare;
	}

	private double calcUserRatingMean() {
		double sum = 0;

		for (YelpRestaurant restaurant: restaurants) {
			sum += restaurant.getPrice();
		}

		return sum/(restaurants.size());
	}


	}
*/
	private double calcPricinessMean(List<YelpRestaurant> userRestaurants) {
		double sum = 0;

		for (YelpRestaurant restaurant: userRestaurants) {
			sum += restaurant.getPrice();
		}

		return sum/(userRestaurants.size());
	}

	private YelpUser getUser(String userID) {
        for (YelpUser user: users) {
            if (userID.equals(user.getUserId())) {
                return user;
            }
        }
    }

	//Returns the review object from the review ID
	private YelpReview getReview(String reviewID) {
        for (YelpReview review: reviews) {
            if (reviewID.equals(review.getReviewId())) {
                return review;
            }
        }
    }

    private YelpRestaurant getRestaurant(String businessID) {
        for (YelpRestaurant restaurant: restaurants) {
            if (businessID.equals(restaurant.getBusinessId())) {
                return restaurant;
            }
        }
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

package ca.ubc.ece.cpen221.mp5.tests;

import static org.junit.Assert.assertTrue;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;

import org.junit.Test;

import ca.ece.ubc.cpen221.mp5.database.Coordinate;
import ca.ece.ubc.cpen221.mp5.database.YelpDatabase;



/**
 * Created by harryyao on 2017-11-27.
 */
public class KmeanTests {
    private String restaurantJSON = "data/restaurants.json";
    private String reviewJSON = "data/reviews.json";
    private String userJSON = "data/users.json";

    @Test
    public void Test00 () {
       int k = 80;
	
	YelpDatabase database = new YelpDatabase(restaurantJSON, reviewJSON, userJSON);
        String jsonCluster = database.kMeansClusters_json(k);

        System.out.println(jsonCluster);

        try(PrintWriter out = new PrintWriter( "visualize/voronoi.json" )  ){
            out.println(jsonCluster);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
       assertTrue(kmeanTester(jsonCluster, k));
    }

    
    
    // requres jsonString is JSON array
    private boolean kmeanTester(String jsonString, int numClusters) {
	JsonReader reader = Json.createReader(new StringReader(jsonString));
	JsonArray jsonArr = reader.readArray();
	
	Map<Integer, List<JsonObject>> clusters = new HashMap<>();
	Map<Integer, Coordinate> clusterCenters = new HashMap<>();

	
	
	for (int i = 0; i < jsonArr.size(); i ++) {
	
	    JsonObject jsonObj = jsonArr.getJsonObject(i);
	    int cluster = jsonObj.getInt("cluster");
	    
	    if (!clusters.containsKey(cluster)) {
		List<JsonObject> list = new ArrayList<>();
		list.add(jsonObj);
		clusters.put(cluster, list);
	    }
	    
	}
	
	if (clusters.keySet().size() != numClusters) {
	    System.out.println(clusters.keySet().size() );
	    return false;
	}
	
	for (int index : clusters.keySet()) {
	    List<JsonObject> list = clusters.get(index);
	    

	    double sumX = 0;
	    for (JsonObject obj : list) {
		sumX += obj.getJsonNumber("x").doubleValue();
		//sumX += Double.parseDouble(obj.getString("x"));
	    }
	    double aveX = sumX / list.size();
	    
	    double sumY = 0;
	    for (JsonObject obj : list) {
		sumY += obj.getJsonNumber("y").doubleValue();
	    }
	    double aveY = sumY / list.size();
	    
	    /*
	    double aveX = list.stream()
		    .mapToDouble(i -> Double.parseDouble(i.getString("x")))
		    .average()
		    .getAsDouble();
	    
	    double aveY = list.stream()
		    .mapToDouble(i -> Double.parseDouble(i.getString("y")))
		    .average()
		    .getAsDouble();
		*/
	    // TODO Coordinate(lat, long) lat is X??? long is Y ?????????? wtffffff
	    Coordinate center = new Coordinate(aveX, aveY);
	    
	    clusterCenters.put(index, center);
	}
	

	for (int i = 0; i < numClusters; i ++) {	    
	    Coordinate center = clusterCenters.get(i);
	    
	    for (JsonObject restaurant : clusters.get(i)) {
		double x = restaurant.getJsonNumber("x").doubleValue();
		double y = restaurant.getJsonNumber("y").doubleValue();
//		double x = Double.parseDouble(restaurant.getString("x"));
//		double y = Double.parseDouble(restaurant.getString("y"));
		double minDist = distToCoordinate(x, y, center);
		
		// compare distance to other centers for each restaurant in this cluster
		for (int j = 0; j < numClusters && j != i; j ++) {
		  //  System.out.println("how many times");
		    
		    if (minDist > distToCoordinate(x, y, clusterCenters.get(j))) {
			//System.out.println(restaurant);
			//System.out.println(minDist + " " + distToCoordinate(x, y, clusterCenters.get(j)));
			
			return false;
			
		    }
		}

	    }
	}
	
	
	
	return true;
	
    }

    // TODO verify x and y
    private double distToCoordinate(double x, double y, Coordinate coordinate) {
	return Math.sqrt(Math.pow(x - coordinate.getLat(), 2) + Math.pow(y - coordinate.getLong(), 2));
    }
    
    
}
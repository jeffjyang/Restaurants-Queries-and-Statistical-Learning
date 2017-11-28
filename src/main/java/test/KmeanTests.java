package test;

import ca.ece.ubc.cpen221.mp5.database.YelpDatabase;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

/**
 * Created by harryyao on 2017-11-27.
 */
public class KmeanTests {
    private String restaurantJSON = "data/restaurants.JSON";
    private String reviewJSON = "data/reviews.json";
    private String userJSON = "data/users.json";

    @Test
    public void Test00 () {
        YelpDatabase database = new YelpDatabase(restaurantJSON, reviewJSON, userJSON);
        String jsonCluster = database.kMeansClusters_json(10);

        System.out.println(jsonCluster);

        try(PrintWriter out = new PrintWriter( "visualize/voronoi.json" )  ){
            out.println(jsonCluster);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


}

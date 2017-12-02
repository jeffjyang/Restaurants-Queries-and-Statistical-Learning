package ca.ubc.ece.cpen221.mp5.tests;

import static org.junit.Assert.assertTrue;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.function.ToDoubleBiFunction;

import org.junit.Test;

import ca.ece.ubc.cpen221.mp5.MP5Db;
import ca.ece.ubc.cpen221.mp5.database.YelpDatabase;
import ca.ece.ubc.cpen221.mp5.database.YelpRestaurant;

public class LeastSquaresRegressionTest {
    private String restaurantJSON = "data/restaurants.json";
    private String reviewJSON = "data/reviews.json";
    private String userJSON = "data/users.json";

    @Test
    public void Test00() {

	YelpDatabase database = new YelpDatabase(restaurantJSON, reviewJSON, userJSON);
	ToDoubleBiFunction<MP5Db<YelpRestaurant>, String> fn = database.getPredictorFunction("cQjFUZ3wHW5MEajjiOw8zw");
	System.out.println(database.getMatches("gclB3ED6uk6viWlolSb_uA").iterator().next().getPrice());
	System.out.println(fn.applyAsDouble(database, "gclB3ED6uk6viWlolSb_uA"));
	System.out.println(fn.applyAsDouble(database, "BJKIoQa5N2T_oDlLVf467Q"));
	assertTrue ( Math.abs(2 * fn.applyAsDouble(database, "gclB3ED6uk6viWlolSb_uA") - fn.applyAsDouble(database, "BJKIoQa5N2T_oDlLVf467Q")) < 10e-5);
    }



}
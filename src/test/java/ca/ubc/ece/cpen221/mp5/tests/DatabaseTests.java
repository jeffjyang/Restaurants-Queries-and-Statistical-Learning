package ca.ubc.ece.cpen221.mp5.tests;

import static org.junit.Assert.*;

import java.util.Set;

import ca.ece.ubc.cpen221.mp5.database.YelpDatabase;
import ca.ece.ubc.cpen221.mp5.database.YelpRestaurant;
import ca.ece.ubc.cpen221.mp5.query.QueryListenerFilterCreator;
import ca.ece.ubc.cpen221.mp5.query.QueryParser.RootContext;

import org.antlr.v4.runtime.ParserRuleContext;
import org.junit.Test;

public class DatabaseTests {
	private String restaurantJSON = "data/restaurants.json";
	private String reviewJSON = "data/reviews.json";
	private String userJSON = "data/users.json";

	Set<YelpRestaurant> restaurants;

	@Test
	public void testQuery() {
		String queryString = "in(Telegraph Ave) && (category(Chinese) || category(Italian)) && price <= 2";
		YelpDatabase database = new YelpDatabase(restaurantJSON, reviewJSON, userJSON);
		Set<YelpRestaurant> restaurants = database.getMatches(queryString);

		YelpRestaurant pastaBene = database.getRestaurant("QQIjsdcokFermi2ugoD6ow");
		YelpRestaurant cafeMilano = database.getRestaurant("NGyFcZHghu1uJ0G-pXJxoQ");

		assertTrue(restaurants.contains(pastaBene));
		assertFalse(restaurants.contains(cafeMilano));
	}

	@Test
	public void testQueryIn() {
		String queryString = "in(Telegraph Ave)";
		YelpDatabase database = new YelpDatabase(restaurantJSON, reviewJSON, userJSON);
		Set<YelpRestaurant> restaurants = database.getMatches(queryString);

		YelpRestaurant pekingExpress = database.getRestaurant("1E2MQLWfwpsId185Fs2gWw");
		YelpRestaurant patBrown = database.getRestaurant("wNTMfBrsvVHleI8cxO1TKw");

		assertTrue(restaurants.contains(pekingExpress));
		assertFalse(restaurants.contains(patBrown));
	}

	@Test
	public void testQueryName() {
		String queryString = "name(Pat Brown's Grill)";
		YelpDatabase database = new YelpDatabase(restaurantJSON, reviewJSON, userJSON);
		Set<YelpRestaurant> restaurants = database.getMatches(queryString);

		YelpRestaurant patBrown = database.getRestaurant("wNTMfBrsvVHleI8cxO1TKw");
		YelpRestaurant cafeMilano = database.getRestaurant("NGyFcZHghu1uJ0G-pXJxoQ");

		assertTrue(restaurants.contains(patBrown));
		assertFalse(restaurants.contains(cafeMilano));
	}

	@Test
	public void testQueryNameDash() {
		String queryString = "name(Crepes A-Go Go)";
		YelpDatabase database = new YelpDatabase(restaurantJSON, reviewJSON, userJSON);
		Set<YelpRestaurant> restaurants = database.getMatches(queryString);

		YelpRestaurant patBrown = database.getRestaurant("ZMqhKMjtdqVZLw11ja3ANg");
		YelpRestaurant cafeMilano = database.getRestaurant("NGyFcZHghu1uJ0G-pXJxoQ");

		assertTrue(restaurants.contains(patBrown));
		assertFalse(restaurants.contains(cafeMilano));
	}

	@Test
	public void testQueryNamePrice() {
		String queryString = "name(Pat Brown's Grill) || price = 3";
		YelpDatabase database = new YelpDatabase(restaurantJSON, reviewJSON, userJSON);
		Set<YelpRestaurant> restaurants = database.getMatches(queryString);

		YelpRestaurant patBrown = database.getRestaurant("wNTMfBrsvVHleI8cxO1TKw");
		YelpRestaurant cafeMilano = database.getRestaurant("NGyFcZHghu1uJ0G-pXJxoQ");
		YelpRestaurant fondueFred = database.getRestaurant("h_we4E3zofRTf4G0JTEF0A");

		assertTrue(restaurants.contains(patBrown));
		assertTrue(restaurants.contains(fondueFred));
		assertFalse(restaurants.contains(cafeMilano));
	}

	@Test
	public void testQueryPrice() {
		String queryString = "price = 3";
		YelpDatabase database = new YelpDatabase(restaurantJSON, reviewJSON, userJSON);
		Set<YelpRestaurant> restaurants = database.getMatches(queryString);

		YelpRestaurant patBrown = database.getRestaurant("wNTMfBrsvVHleI8cxO1TKw");
		YelpRestaurant cafeMilano = database.getRestaurant("NGyFcZHghu1uJ0G-pXJxoQ");
		YelpRestaurant fondueFred = database.getRestaurant("h_we4E3zofRTf4G0JTEF0A");

		assertFalse(restaurants.contains(patBrown));
		assertTrue(restaurants.contains(fondueFred));
		assertFalse(restaurants.contains(cafeMilano));
	}

	@Test
	public void testQueryRating() {
		String queryString = "rating = 3.5";
		YelpDatabase database = new YelpDatabase(restaurantJSON, reviewJSON, userJSON);
		Set<YelpRestaurant> restaurants = database.getMatches(queryString);

		YelpRestaurant pekingExpress = database.getRestaurant("1E2MQLWfwpsId185Fs2gWw");
		YelpRestaurant cafeMilano = database.getRestaurant("NGyFcZHghu1uJ0G-pXJxoQ");
		YelpRestaurant fondueFred = database.getRestaurant("h_we4E3zofRTf4G0JTEF0A");

		assertTrue(restaurants.contains(pekingExpress));
		assertFalse(restaurants.contains(fondueFred));
		assertTrue(restaurants.contains(cafeMilano));
	}

	@Test
	public void testQueryRateLTIneq() {
		String queryString = "price < 2 || rating < 2";
		YelpDatabase database = new YelpDatabase(restaurantJSON, reviewJSON, userJSON);
		Set<YelpRestaurant> restaurants = database.getMatches(queryString);

		assertTrue(restaurants != null);
	}

	@Test
	public void testQueryRateLTEIneq() {
		String queryString = "price <= 2 || rating <= 2";
		YelpDatabase database = new YelpDatabase(restaurantJSON, reviewJSON, userJSON);
		Set<YelpRestaurant> restaurants = database.getMatches(queryString);

		assertTrue(restaurants != null);
	}

	@Test
	public void testQueryRateGTIneq() {
		String queryString = "price > 2 || rating > 2";
		YelpDatabase database = new YelpDatabase(restaurantJSON, reviewJSON, userJSON);
		Set<YelpRestaurant> restaurants = database.getMatches(queryString);

		assertTrue(restaurants != null);
	}

	@Test
	public void testQueryRateGTEIneq() {
		String queryString = "price >= 2 || rating >= 2";
		YelpDatabase database = new YelpDatabase(restaurantJSON, reviewJSON, userJSON);
		Set<YelpRestaurant> restaurants = database.getMatches(queryString);

		assertTrue(restaurants != null);
	}

	@Test
	public void testQueryRateEQIneq() {
		String queryString = "(price = 3 && price <= 3) || (price = 2 || rating = 2)";
		YelpDatabase database = new YelpDatabase(restaurantJSON, reviewJSON, userJSON);
		Set<YelpRestaurant> restaurants = database.getMatches(queryString);

		assertTrue(restaurants != null);
	}

	@Test
	public void testQueryRateExtraIneq() {
		String queryString = "in(UC Campus Area) && (price <= 2 || rating = 3)";
		YelpDatabase database = new YelpDatabase(restaurantJSON, reviewJSON, userJSON);
		Set<YelpRestaurant> restaurants = database.getMatches(queryString);
		
		
		YelpRestaurant bearRamen = database.getRestaurant("9N684D-RFrQC0V4K0XvdxQ");
		YelpRestaurant Babette = database.getRestaurant("6QZR4ToHKlse0yhqpU5ijg");
		assertTrue(restaurants.contains(bearRamen));
		assertFalse(restaurants.contains(Babette));
	}

	@Test
	public void testQueryComprehensive() {
		String queryString = "name(Camille) || (in(UC Campus Area) && category(Japanese))";
		YelpDatabase database = new YelpDatabase(restaurantJSON, reviewJSON, userJSON);
		Set<YelpRestaurant> restaurants = database.getMatches(queryString);

		YelpRestaurant takoSushi = database.getRestaurant("2ciUQ05DREauhBC3xiA4qw");
		YelpRestaurant cafeMilano = database.getRestaurant("NGyFcZHghu1uJ0G-pXJxoQ");
		YelpRestaurant Camille = database.getRestaurant("z4YO4rycvYrZEd3VYtzVvw");

		assertTrue(restaurants.contains(takoSushi));
		assertTrue(restaurants.contains(Camille));
		assertFalse(restaurants.contains(cafeMilano));
	}

	@Test
	public void testQueryNone() {
		String queryString = "name(nothing)";
		YelpDatabase database = new YelpDatabase(restaurantJSON, reviewJSON, userJSON);
		Set<YelpRestaurant> restaurants = database.getMatches(queryString);

		YelpRestaurant takoSushi = database.getRestaurant("2ciUQ05DREauhBC3xiA4qw");
		YelpRestaurant cafeMilano = database.getRestaurant("NGyFcZHghu1uJ0G-pXJxoQ");
		YelpRestaurant Camille = database.getRestaurant("z4YO4rycvYrZEd3VYtzVvw");

		assertTrue(restaurants.size() == 0);
		assertFalse(restaurants.contains(takoSushi));
		assertFalse(restaurants.contains(Camille));
		assertFalse(restaurants.contains(cafeMilano));
	}
	
	@Test
	public void testQueryBracket() {
		String queryString = "(name(Mandarin House) && price <= 4) || (category(Chinese) || "
				+ "in(Telegraph Ave))";
		YelpDatabase database = new YelpDatabase(restaurantJSON, reviewJSON, userJSON);
		Set<YelpRestaurant> restaurants = database.getMatches(queryString);

		YelpRestaurant mandarinHouse = database.getRestaurant("5fneYCWLhgBZQUcNPOch-w");
		YelpRestaurant Camille = database.getRestaurant("z4YO4rycvYrZEd3VYtzVvw");

		assertTrue(restaurants.contains(mandarinHouse));
		assertFalse(restaurants.contains(Camille));
	}

	@Test
	public void testQueryInvalidSyntax() {
		String queryString = "garbagef";
		YelpDatabase database = new YelpDatabase(restaurantJSON, reviewJSON, userJSON);
		Set<YelpRestaurant> restaurants = database.getMatches(queryString);

		assertTrue(restaurants == null);
	}
	
	@Test
	public void exitLoopNull() {
		ParserRuleContext ctxParser = new ParserRuleContext();
		RootContext ctx = new RootContext(ctxParser, 0);
		YelpDatabase database = new YelpDatabase(restaurantJSON, reviewJSON, userJSON);
		QueryListenerFilterCreator fc = new QueryListenerFilterCreator(database.getRestaurants());
		
		fc.exitRoot(ctx);
		Set<YelpRestaurant> filteredRestaurants = fc.getQueryRestaurants();
		assertTrue(filteredRestaurants == null);
	}
}

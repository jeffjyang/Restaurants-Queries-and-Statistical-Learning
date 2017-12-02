package ca.ece.ubc.cpen221.mp5.query;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;
import java.util.stream.Collectors;

import ca.ece.ubc.cpen221.mp5.database.YelpRestaurant;
import ca.ece.ubc.cpen221.mp5.query.QueryParser.AndexprContext;
import ca.ece.ubc.cpen221.mp5.query.QueryParser.CategoryContext;
import ca.ece.ubc.cpen221.mp5.query.QueryParser.InContext;
import ca.ece.ubc.cpen221.mp5.query.QueryParser.NameContext;
import ca.ece.ubc.cpen221.mp5.query.QueryParser.OrexprContext;
import ca.ece.ubc.cpen221.mp5.query.QueryParser.PriceContext;
import ca.ece.ubc.cpen221.mp5.query.QueryParser.RatingContext;
import ca.ece.ubc.cpen221.mp5.query.QueryParser.RootContext;

public class QueryListenerFilterCreator extends QueryBaseListener {
	private Stack<Set<YelpRestaurant>> restaurantStack = new Stack<>();
	private List<YelpRestaurant> restaurantsList;
	private Set<YelpRestaurant> filteredRestaurants;

	/**
	 * Constructs a QueryListener from a set of restaurants
	 * 
	 * @param restaurants
	 *            from the total list of restaurants
	 */
	public QueryListenerFilterCreator(Set<YelpRestaurant> restaurants) {
		super();
		restaurantsList = new ArrayList<>(restaurants);
	}

	/**
	 * Making the set of filtered restaurants the top/last element of the stack
	 * 
	 * @param RootContext
	 *            grammar for root
	 */
	@Override
	public void exitRoot(RootContext ctx) {
		if (!restaurantStack.isEmpty()) { //Checking that stack is not empty
			Set<YelpRestaurant> combinedSet = restaurantStack.pop(); //Popping and making result equal to last list
			filteredRestaurants = combinedSet;
		} else {
			filteredRestaurants = null;
		}
	}

	/**
	 * Replacing the OR sets to be the union of the two sets
	 * 
	 * @param OrexprContext
	 *            grammar for ORexpr
	 */
	@Override
	public void exitOrexpr(OrexprContext ctx) {
		if (restaurantStack.size() > 1) {
			Set<YelpRestaurant> s1 = restaurantStack.pop(); //Popping and calculating union
			Set<YelpRestaurant> s2 = restaurantStack.pop();
			Set<YelpRestaurant> s1ORs2 = new HashSet<>();

			s1ORs2.addAll(s1); //Union operations
			s1ORs2.addAll(s2);
			restaurantStack.push(s1ORs2); //Pushing back to stack
		}
	}

	/**
	 * Replacing the AND sets to be the intersection of the two sets
	 * 
	 * @param AndexprContext
	 *            grammar for ANDexpr
	 */
	@Override
	public void exitAndexpr(AndexprContext ctx) {
		if (restaurantStack.size() > 1) {
			Set<YelpRestaurant> s1 = restaurantStack.pop(); //Popping top two sets
			Set<YelpRestaurant> s2 = restaurantStack.pop();
			Set<YelpRestaurant> s1ANDs2 = new HashSet<>(s1); 

			s1ANDs2.retainAll(s2); //Logical And operation
			restaurantStack.push(s1ANDs2);
		}
	}

	/**
	 * Adds a set of the restaurants in the given neighbor dictated by ctx to the
	 * stack
	 * 
	 * @param InContext
	 *            defines the grammar for In
	 */
	@Override
	public void exitIn(InContext ctx) {
		String neighborhood = ctx.STRING().toString().replaceAll("_", " "); // Replacing underscore with space
		Set<YelpRestaurant> neighborhoodRestaurants = new HashSet<>();

		for (YelpRestaurant restaurant : restaurantsList) { //Adding every restaurant matching neighborhood
			List<String> neighborhoods = restaurant.getNeighborhoods();
			for (String neighborhoodString : neighborhoods) { //Checking neighborhoods
				neighborhoodString = neighborhoodString.replaceAll("\"", ""); 
				if (neighborhoodString.equals(neighborhood))
					neighborhoodRestaurants.add(restaurant);
			}
		}
		restaurantStack.push(neighborhoodRestaurants);
	}

	/**
	 * Adds a set of the restaurants in the given category dictated by ctx to the
	 * stack
	 * 
	 * @param CategoryContext
	 *            defines the grammar for Category
	 */
	@Override
	public void exitCategory(CategoryContext ctx) {
		String category = ctx.STRING().toString().replaceAll("_", " "); //Replacing underscore for space
		Set<YelpRestaurant> categoryRestaurants = new HashSet<>();

		for (YelpRestaurant restaurant : restaurantsList) { //Adding every restaurant that matches category
			List<String> categories = restaurant.getCategories();

			for (String categoryString : categories) { //Checking for category
				categoryString = categoryString.replaceAll("\"", "");
				if (categoryString.equals(category))
					categoryRestaurants.add(restaurant);
			}
		}
		restaurantStack.push(categoryRestaurants);
	}

	/**
	 * Adds a set of the restaurants with the name dictated by ctx to the stack
	 * 
	 * @param NameContext
	 *            defines the grammar for Name
	 */
	@Override
	public void exitName(NameContext ctx) {
		final String name = ctx.STRING().toString().replaceAll("_", " ");

		Set<YelpRestaurant> nameRestaurants = restaurantsList.stream() //Filtering by names
				.filter(restaurant -> restaurant.getName().equals(name)).collect(Collectors.toSet());
		restaurantStack.push(nameRestaurants);
	}

	/**
	 * Adds a set of the restaurants with the given rating and operation dictated by
	 * ctx to the stack
	 * 
	 * @param RatingContext
	 *            defines the grammar for the Rating
	 */
	@Override
	public void exitRating(RatingContext ctx) {
		String operation = ctx.INEQ().toString();
		double rating = Double.parseDouble(ctx.NUM().toString());
		Set<YelpRestaurant> ratingRestaurants = null;

		switch (operation) { //Performs operation based on the operation required
		case "<=": 
			ratingRestaurants = restaurantsList.stream().filter(restaurant -> restaurant.getStars() <= rating)
					.collect(Collectors.toSet()); //Uses streams to filter the restaurant lists
			break;
		case "<":
			ratingRestaurants = restaurantsList.stream().filter(restaurant -> restaurant.getStars() < rating)
					.collect(Collectors.toSet());
			break;
		case ">":
			ratingRestaurants = restaurantsList.stream().filter(restaurant -> restaurant.getStars() > rating)
					.collect(Collectors.toSet());
			break;
		case ">=":
			ratingRestaurants = restaurantsList.stream().filter(restaurant -> restaurant.getStars() >= rating)
					.collect(Collectors.toSet());
			break;
		case "=":
			ratingRestaurants = restaurantsList.stream().filter(restaurant -> restaurant.getStars() == rating)
					.collect(Collectors.toSet());
			break;
		}
		restaurantStack.push(ratingRestaurants);
	}

	/**
	 * Adds a set of the restaurants with the given price and operation dictated by
	 * ctx to the stack
	 * 
	 * @param PriceContext
	 *            defines the grammar for the Price
	 */
	@Override
	public void exitPrice(PriceContext ctx) {
		String operation = ctx.INEQ().toString();
		int price = Integer.parseInt(ctx.NUM().toString());
		Set<YelpRestaurant> priceRestaurants = null;

		switch (operation) { //Filtering price restaurant by operation required
		case "<=":
			priceRestaurants = restaurantsList.stream().filter(restaurant -> restaurant.getPrice() <= price)
					.collect(Collectors.toSet());
			break;
		case "<":
			priceRestaurants = restaurantsList.stream().filter(restaurant -> restaurant.getPrice() < price)
					.collect(Collectors.toSet());
			break;
		case ">":
			priceRestaurants = restaurantsList.stream().filter(restaurant -> restaurant.getPrice() > price)
					.collect(Collectors.toSet());
			break;
		case ">=":
			priceRestaurants = restaurantsList.stream().filter(restaurant -> restaurant.getPrice() >= price)
					.collect(Collectors.toSet());
			break;
		case "=":
			priceRestaurants = restaurantsList.stream().filter(restaurant -> restaurant.getPrice() == price)
					.collect(Collectors.toSet());
			break;
		}
		restaurantStack.push(priceRestaurants);
	}

	/**
	 * Returns the filtered set of restaurants
	 * 
	 * @return Set<YelpRestaurant> set containing the filtered restaurant matching
	 *         the query
	 */
	public Set<YelpRestaurant> getQueryRestaurants() {
		return filteredRestaurants;
	}
}

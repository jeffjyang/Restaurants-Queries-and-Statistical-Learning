package ca.ece.ubc.cpen221.mp5.query;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.antlr.v4.runtime.RuleContext;

import ca.ece.ubc.cpen221.mp5.database.YelpRestaurant;
import ca.ece.ubc.cpen221.mp5.query.QueryParser.AndexprContext;
import ca.ece.ubc.cpen221.mp5.query.QueryParser.AtomContext;
import ca.ece.ubc.cpen221.mp5.query.QueryParser.CategoryContext;
import ca.ece.ubc.cpen221.mp5.query.QueryParser.InContext;
import ca.ece.ubc.cpen221.mp5.query.QueryParser.NameContext;
import ca.ece.ubc.cpen221.mp5.query.QueryParser.OrexprContext;
import ca.ece.ubc.cpen221.mp5.query.QueryParser.PriceContext;
import ca.ece.ubc.cpen221.mp5.query.QueryParser.RatingContext;
import ca.ece.ubc.cpen221.mp5.query.QueryParser.RootContext;

public class QueryListenerFilterCreator extends QueryBaseListener {
	Stack<Set<YelpRestaurant>> restaurantStack = new Stack<>();
	Set<YelpRestaurant> restaurants;
	List<YelpRestaurant> restaurantsList;
	Set<YelpRestaurant> filteredRestaurants;

	public QueryListenerFilterCreator(Set<YelpRestaurant> restaurants) {
		super();
		this.restaurants = restaurants;
		restaurantsList = new ArrayList<>(restaurants);
	}

	@Override
	public void exitRoot(RootContext ctx) {
//		System.err.println("exiting root");

		//System.out.println(restaurantStack.size());

		Set<YelpRestaurant> combinedSet = restaurantStack.pop();
		//System.out.println("CombinedSet" + combinedSet.size());
		for (YelpRestaurant rest : combinedSet) {
		//	System.out.println(rest.getName());
		}
		filteredRestaurants = combinedSet;
	}

	@Override
	public void enterOrexpr(OrexprContext ctx) {
//		System.err.println("entering or");
	}

	@Override
	public void exitOrexpr(OrexprContext ctx) {
//		System.err.println("exiting or");
		if (restaurantStack.size() > 1) {
			Set<YelpRestaurant> s1 = restaurantStack.pop();
			Set<YelpRestaurant> s2 = restaurantStack.pop();
			Set<YelpRestaurant> s1ORs2 = new HashSet<>();
			s1ORs2.addAll(s1);
			s1ORs2.addAll(s2);
			restaurantStack.push(s1ORs2);
		}
	}
	@Override
	public void exitAndexpr(AndexprContext ctx) {
//		System.err.println("exiting and");
	//	System.out.println("and" + ctx.getChildCount());
	//	System.out.println(restaurantStack.size());
		if (restaurantStack.size() > 1) {
			Set<YelpRestaurant> s1 = restaurantStack.pop();
			Set<YelpRestaurant> s2 = restaurantStack.pop();
			/*
			for (YelpRestaurant rest : s1) {
				System.out.println(rest.getName());
			}
			for (YelpRestaurant rest : s2) {
				System.out.println(rest.getName());
			}
			*/
			
			Set<YelpRestaurant> s1ANDs2 = new HashSet<>(s1);
			s1ANDs2.retainAll(s2);

			restaurantStack.push(s1ANDs2);
		}
	}
	@Override
	public void exitIn(InContext ctx) {
//		System.err.println("exiting in");
		String neighborhood = ctx.STRING().toString();
		neighborhood = neighborhood.replaceAll("_", " "); //Replacing underscore with space
		//System.out.println(neighborhood);
		Set<YelpRestaurant> neighborhoodRestaurants = new HashSet<>();
		/*
		 * Set<YelpRestaurant> categoryRestaurants = restaurantsList.stream()
		 * .filter(restaurant -> restaurant.getCategories().contains(category))
		 * .collect(Collectors.toSet());
		 */
		for (YelpRestaurant restaurant : restaurantsList) {
			List<String> neighborhoods = restaurant.getNeighborhoods();
		//	System.out.println(neighborhoods);
			for (String neighborhoodString : neighborhoods) {
				neighborhoodString = neighborhoodString.replaceAll("\"", "");
			//	 System.out.println(neighborhoodString + "Check" + neighborhood);
				if (neighborhoodString.equals(neighborhood))
					neighborhoodRestaurants.add(restaurant);
			}
		}
		for (YelpRestaurant restaurant : neighborhoodRestaurants) {
		//	System.out.println(restaurant.getName());
		}
	//	System.out.println(neighborhoodRestaurants.size());
		restaurantStack.push(neighborhoodRestaurants);
		
	}

	@Override
	public void exitCategory(CategoryContext ctx) {
//		System.err.println("exiting category");
		String category = ctx.STRING().toString();
		// System.out.println("Looking for" + category);
		Set<YelpRestaurant> categoryRestaurants = new HashSet<>();
		/*
		 * Set<YelpRestaurant> categoryRestaurants = restaurantsList.stream()
		 * .filter(restaurant -> restaurant.getCategories().contains(category))
		 * .collect(Collectors.toSet());
		 */
		for (YelpRestaurant restaurant : restaurantsList) {
			List<String> categories = restaurant.getCategories();

			for (String categoryString : categories) {
				categoryString = categoryString.replaceAll("\"", "");
				// System.out.println(categoryString + "Check" + category);
				if (categoryString.equals(category))
					categoryRestaurants.add(restaurant);
			}
		}
		for (YelpRestaurant restaurant : categoryRestaurants) {
			//System.out.println(restaurant.getName());
		}
		restaurantStack.push(categoryRestaurants);
	}


	@Override
	public void exitName(NameContext ctx) {
//		System.err.println("exiting name" + ctx.toString());
		Set<YelpRestaurant> nameRestaurants = restaurantsList.stream()
				.filter(restaurant -> restaurant.getName().equals(ctx.toString())).collect(Collectors.toSet());
		restaurantStack.push(nameRestaurants);

		// System.out.println(nameRestaurants.toString());
	}

	@Override
	public void enterPrice(PriceContext ctx) {
//		System.err.println("entering price");
	}

	@Override
	public void exitPrice(PriceContext ctx) {
//		System.err.println("exiting price" + ctx.NUM());
		String operation = ctx.INEQ().toString();
		int price = Integer.parseInt(ctx.NUM().toString());
//		System.err.println(operation);
		Set<YelpRestaurant> priceRestaurants;

		switch (operation) {
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
		default:
			priceRestaurants = null;
		}

		/*
		 * for (YelpRestaurant restaurant: priceRestaurants) {
		 * System.out.println(restaurant.getName()); }
		 */
		restaurantStack.push(priceRestaurants);
	}
	public Set<YelpRestaurant> getQueryRestaurants() {
		return filteredRestaurants;
	}
}

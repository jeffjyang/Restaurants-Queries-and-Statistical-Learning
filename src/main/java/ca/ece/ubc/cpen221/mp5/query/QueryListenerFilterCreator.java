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
	
	public QueryListenerFilterCreator (Set<YelpRestaurant> restaurants) {
		super();
		this.restaurants = restaurants;
		restaurantsList = new ArrayList<>(restaurants);
	}
	
	
	@Override
	public void enterRoot(RootContext ctx) {
		System.err.println("entering root");
	}

	@Override
	public void exitRoot(RootContext ctx) {
		System.err.println("exiting root");
	}

	@Override
	public void enterOrexpr(OrexprContext ctx) {
		System.err.println("entering or");
	}

	@Override
	public void exitOrexpr(OrexprContext ctx) {
		System.err.println("exiting or");
	}

	@Override
	public void enterAndexpr(AndexprContext ctx) {
		System.err.println("entering and");
	}

	@Override
	public void exitAndexpr(AndexprContext ctx) {
		System.err.println("exiting and");
	}

	@Override
	public void enterAtom(AtomContext ctx) {
		System.err.println("entering atom");
	}

	@Override
	public void exitAtom(AtomContext ctx) {
		System.err.println("exiting atom");;
	}

	@Override
	public void enterIn(InContext ctx) {
		
	}

	@Override
	public void exitIn(InContext ctx) {
		System.err.println("exiting ib");
	}

	@Override
	public void enterCategory(CategoryContext ctx) {
		System.err.println("entering category");
		
		
		
	}

	@Override
	public void exitCategory(CategoryContext ctx) {
		System.err.println("exiting category");
		String category = ctx.STRING().toString();
		System.out.println("Looking for" + category);
		Set<YelpRestaurant> categoryRestaurants = new HashSet<>();
		/*Set<YelpRestaurant> categoryRestaurants = restaurantsList.stream()
				.filter(restaurant -> restaurant.getCategories().contains(category))
				.collect(Collectors.toSet());
		*/
		for (YelpRestaurant restaurant: restaurantsList) {
			List<String> categories = restaurant.getCategories();
		//	System.out.println(categories.size());
		//	System.out.println(categories);
			
			for (String categoryString : categories) {
				if (categoryString.equals(category)) categoryRestaurants.add(restaurant)
			}
			
			if (categories.contains(category)) {
				System.out.println("found");
				categoryRestaurants.add(restaurant);
			}
		}
		
		
		
		for (YelpRestaurant restaurant: categoryRestaurants) {
			System.out.println(restaurant.getName());
		}
		restaurantStack.push(categoryRestaurants);
	}

	@Override
	public void enterName(NameContext ctx) {
		System.err.println("entering name");
	}

	@Override
	public void exitName(NameContext ctx) {
		System.err.println("exiting name" + ctx.toString());
		Set<YelpRestaurant> nameRestaurants = restaurantsList.stream()
				.filter(restaurant -> restaurant.getName().equals(ctx.toString()))
				.collect(Collectors.toSet());
		restaurantStack.push(nameRestaurants);
		
	//	System.out.println(nameRestaurants.toString());
		}

	@Override
	public void enterPrice(PriceContext ctx) {
		System.err.println("entering price");
	}

	@Override
	public void exitPrice(PriceContext ctx) {
		System.err.println("exiting price" + ctx.NUM());
		int price = Integer.parseInt(ctx.NUM().toString());
		
		System.err.println("HI" + ctx.toString());
		Set<YelpRestaurant> priceRestaurants = restaurantsList.stream()
				.filter(restaurant -> restaurant.getPrice() == price)
				.collect(Collectors.toSet());
		/*for (YelpRestaurant restaurant: priceRestaurants) {
			System.out.println(restaurant.getName());
		}*/
		restaurantStack.push(priceRestaurants);
	}

	@Override
	public void enterRating(RatingContext ctx) {
		System.err.println("entering rating");
	}

}

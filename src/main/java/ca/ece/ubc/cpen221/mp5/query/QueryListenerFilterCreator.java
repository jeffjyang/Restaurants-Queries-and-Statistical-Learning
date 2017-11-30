package ca.ece.ubc.cpen221.mp5.query;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
	List<YelpRestaurant> restaurantsList = new ArrayList<>(restaurants);
	
	public QueryListenerFilterCreator (Set<YelpRestaurant> restaurants) {
		this.restaurants = restaurants;
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
	}

	@Override
	public void enterName(NameContext ctx) {
		System.err.println("entering name");
	}

	@Override
	public void exitName(NameContext ctx) {
		System.err.println("exiting name");
		Set<YelpRestaurant> restaurants = restaurantsList.stream()
				.filter(restaurant -> restaurant.getName().equals(ctx.toString()))
				.collect(Collectors.toSet());
		restaurantStack.push(restaurants);
		}

	@Override
	public void enterPrice(PriceContext ctx) {
		System.err.println("entering price");
	}

	@Override
	public void exitPrice(PriceContext ctx) {
		System.err.println("exiting price");
		Set<YelpRestaurant> restaurants = restaurantsList.stream()
				.filter(restaurant -> ctx.toString().equals(restaurant.getPrice()))
				.collect(Collectors.toSet());
		restaurantStack.push(restaurants);
		}
	}

	@Override
	public void enterRating(RatingContext ctx) {
		System.err.println("entering rating");
	}

	@Override
	public void exitRating(RatingContext ctx) {
		System.err.println("exiting rating");
	}
}

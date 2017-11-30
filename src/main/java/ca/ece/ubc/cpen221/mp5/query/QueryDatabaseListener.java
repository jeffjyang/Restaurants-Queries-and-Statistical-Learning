package ca.ece.ubc.cpen221.mp5.query;

import java.util.Collections;
import java.util.Set;
import java.util.Stack;

import ca.ece.ubc.cpen221.mp5.database.YelpDatabase;
import ca.ece.ubc.cpen221.mp5.database.YelpRestaurant;

public class QueryDatabaseListener extends QueryBaseListener{
	private Stack<Set<YelpRestaurant>> stack = new Stack<Set<YelpRestaurant>>();
	private YelpDatabase database;
	
	public QueryDatabaseListener(YelpDatabase database) {
		this.database = database;
	}
	
	@Override
	public void exitOrexpr(QueryParser.OrexprContext ctx) {
		if (ctx.OR() != null) {
			
		}
	}
	
	@Override
	public void exitAndexpr(QueryParser.AndexprContext ctx) {
		if (ctx.AND() != null) {
			
		}
	}
	
	@Override
	public void exitIn(QueryParser.InContext ctx) {
		Set<YelpRestaurant> result;
		result = Collections.synchronizedSet(database.filterRestaurantNeighbourhood(ctx.getText()));\
		System.out
	}
	
	@Override
	public void exitCategory(QueryParser.CategoryContext ctx) {
		
	}
	@Override
	public void exitName(QueryParser.NameContext ctx) {
		
	}
	@Override
	public void exitPrice(QueryParser.PriceContext ctx) {
		
	}
	@Override
	public void exitRating(QueryParser.RatingContext ctx) {
		
	}
	
}

package ca.ece.ubc.cpen221.mp5.query;


import ca.ece.ubc.cpen221.mp5.query.QueryParser.AndexprContext;
import ca.ece.ubc.cpen221.mp5.query.QueryParser.AtomContext;
import ca.ece.ubc.cpen221.mp5.query.QueryParser.CategoryContext;
import ca.ece.ubc.cpen221.mp5.query.QueryParser.InContext;
import ca.ece.ubc.cpen221.mp5.query.QueryParser.NameContext;
import ca.ece.ubc.cpen221.mp5.query.QueryParser.OrexprContext;
import ca.ece.ubc.cpen221.mp5.query.QueryParser.PriceContext;
import ca.ece.ubc.cpen221.mp5.query.QueryParser.RatingContext;
import ca.ece.ubc.cpen221.mp5.query.QueryParser.RootContext;

public class QueryListenerPrintEverything extends QueryBaseListener {

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
		System.err.println("entering in");
	}

	@Override
	public void exitIn(InContext ctx) {
		System.err.println("exiting in");
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
	}

	@Override
	public void enterPrice(PriceContext ctx) {
		System.err.println("entering price");
	}

	@Override
	public void exitPrice(PriceContext ctx) {
		System.err.println("exiting price");
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

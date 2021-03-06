// Generated from C:/Users/Harry/Documents/GitHub/f17-mp5-yaoharry_jeffjyang/src/main/java/ca/ece/ubc/cpen221/mp5/query/Query.g4 by ANTLR 4.7

package ca.ece.ubc.cpen221.mp5.query;

import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link QueryParser}.
 */
public interface QueryListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link QueryParser#root}.
	 * @param ctx the parse tree
	 */
	void enterRoot(QueryParser.RootContext ctx);
	/**
	 * Exit a parse tree produced by {@link QueryParser#root}.
	 * @param ctx the parse tree
	 */
	void exitRoot(QueryParser.RootContext ctx);
	/**
	 * Enter a parse tree produced by {@link QueryParser#orexpr}.
	 * @param ctx the parse tree
	 */
	void enterOrexpr(QueryParser.OrexprContext ctx);
	/**
	 * Exit a parse tree produced by {@link QueryParser#orexpr}.
	 * @param ctx the parse tree
	 */
	void exitOrexpr(QueryParser.OrexprContext ctx);
	/**
	 * Enter a parse tree produced by {@link QueryParser#andexpr}.
	 * @param ctx the parse tree
	 */
	void enterAndexpr(QueryParser.AndexprContext ctx);
	/**
	 * Exit a parse tree produced by {@link QueryParser#andexpr}.
	 * @param ctx the parse tree
	 */
	void exitAndexpr(QueryParser.AndexprContext ctx);
	/**
	 * Enter a parse tree produced by {@link QueryParser#atom}.
	 * @param ctx the parse tree
	 */
	void enterAtom(QueryParser.AtomContext ctx);
	/**
	 * Exit a parse tree produced by {@link QueryParser#atom}.
	 * @param ctx the parse tree
	 */
	void exitAtom(QueryParser.AtomContext ctx);
	/**
	 * Enter a parse tree produced by {@link QueryParser#in}.
	 * @param ctx the parse tree
	 */
	void enterIn(QueryParser.InContext ctx);
	/**
	 * Exit a parse tree produced by {@link QueryParser#in}.
	 * @param ctx the parse tree
	 */
	void exitIn(QueryParser.InContext ctx);
	/**
	 * Enter a parse tree produced by {@link QueryParser#category}.
	 * @param ctx the parse tree
	 */
	void enterCategory(QueryParser.CategoryContext ctx);
	/**
	 * Exit a parse tree produced by {@link QueryParser#category}.
	 * @param ctx the parse tree
	 */
	void exitCategory(QueryParser.CategoryContext ctx);
	/**
	 * Enter a parse tree produced by {@link QueryParser#name}.
	 * @param ctx the parse tree
	 */
	void enterName(QueryParser.NameContext ctx);
	/**
	 * Exit a parse tree produced by {@link QueryParser#name}.
	 * @param ctx the parse tree
	 */
	void exitName(QueryParser.NameContext ctx);
	/**
	 * Enter a parse tree produced by {@link QueryParser#price}.
	 * @param ctx the parse tree
	 */
	void enterPrice(QueryParser.PriceContext ctx);
	/**
	 * Exit a parse tree produced by {@link QueryParser#price}.
	 * @param ctx the parse tree
	 */
	void exitPrice(QueryParser.PriceContext ctx);
	/**
	 * Enter a parse tree produced by {@link QueryParser#rating}.
	 * @param ctx the parse tree
	 */
	void enterRating(QueryParser.RatingContext ctx);
	/**
	 * Exit a parse tree produced by {@link QueryParser#rating}.
	 * @param ctx the parse tree
	 */
	void exitRating(QueryParser.RatingContext ctx);
}
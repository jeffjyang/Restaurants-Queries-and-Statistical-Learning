package test;

import java.util.Arrays;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.antlr.v4.gui.TreeViewer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.junit.Test;

import ca.ece.ubc.cpen221.mp5.database.YelpDatabase;
import ca.ece.ubc.cpen221.mp5.query.Query;
import ca.ece.ubc.cpen221.mp5.query.QueryLexer;
import ca.ece.ubc.cpen221.mp5.query.QueryListener;
import ca.ece.ubc.cpen221.mp5.query.QueryListenerPrintEverything;
import ca.ece.ubc.cpen221.mp5.query.QueryParser;

public class QueryTests {
	private final static String restaurantJSON = "data/restaurants.JSON";
	private final static String reviewJSON = "data/reviews.json";
	private final static String userJSON = "data/users.json";

	@Test
	public void Test00() {
		YelpDatabase database = new YelpDatabase(restaurantJSON, reviewJSON, userJSON);
		String query = "category(Chinese)";
		Query queryObj = new Query();

		queryObj.queryDatabase(database, query);

	}

	// Taken from
	// https://stackoverflow.com/questions/23809005/how-to-display-antlr-tree-gui
	public static void main(String[] args) {
		String query = "(category(Chinese) || category(Italian)) && price <= 2";
		CharStream stream = CharStreams.fromString(query);
		QueryLexer lexer = new QueryLexer(stream);
		TokenStream tokens = new CommonTokenStream(lexer);
		QueryParser parser = new QueryParser(tokens);
		ParseTree tree = parser.root();
		ParseTreeWalker walker = new ParseTreeWalker();
		QueryListener listener = new QueryListenerPrintEverything();
		walker.walk(listener, tree);
		System.out.println(tree.toStringTree(parser));

		// show AST in GUI
		JFrame frame = new JFrame("Antlr AST");
		JPanel panel = new JPanel();
		TreeViewer viewr = new TreeViewer(Arrays.asList(parser.getRuleNames()), tree);
		viewr.setScale(1.5);// scale a little
		panel.add(viewr);
		frame.add(panel);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(200, 200);
		frame.setVisible(true);
	}

}

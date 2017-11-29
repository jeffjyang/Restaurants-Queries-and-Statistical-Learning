package ca.ece.ubc.cpen221.mp5.query;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.Trees;


import ca.ece.ubc.cpen221.mp5.database.YelpDatabase;

/**
 * Created by harryyao on 2017-11-28.
 */
public class Query {
    public void queryDatabase(YelpDatabase database, String query) {
		CharStream stream = CharStreams.fromString(query);
    	
    	QueryLexer lexer = new QueryLexer(stream);
    	TokenStream tokens = new CommonTokenStream(lexer);
    	QueryParser parser = new QueryParser(tokens);
    	
    	ParseTree tree = parser.root();
    //	System.err.println(tree.toStringTree(parser));
    	/show AST in GUI
    	JFrame frame = new JFrame("Antlr AST");
    	JPanel panel = new JPanel();
    	TreeViewer viewr = new TreeViewer(Arrays.asList(
    			parser.getRuleNames()),tree);
    	viewr.setScale(1.5);//scale a little

    	panel.add(viewr);
    	frame.add(panel);
    	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	frame.setSize(500,500);
    	frame.setVisible(true);
    }
}

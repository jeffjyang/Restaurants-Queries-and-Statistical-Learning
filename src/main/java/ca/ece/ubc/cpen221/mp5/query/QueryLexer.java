// Generated from Query.g4 by ANTLR 4.7

package ca.ece.ubc.cpen221.mp5.query;

import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class QueryLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.7", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		OR=1, AND=2, LPAREN=3, RPAREN=4, IN=5, NUM=6, CATEGORY=7, NAME=8, RATING=9, 
		PRICE=10, STRING=11, WHITESPACE=12, INEQ=13;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	public static final String[] ruleNames = {
		"OR", "AND", "LPAREN", "RPAREN", "IN", "NUM", "CATEGORY", "NAME", "RATING", 
		"PRICE", "STRING", "WHITESPACE", "INEQ"
	};

	private static final String[] _LITERAL_NAMES = {
		null, "'||'", "'&&'", "'('", "')'", "'in'", null, "'category'", "'name'", 
		"'rating'", "'price'"
	};
	private static final String[] _SYMBOLIC_NAMES = {
		null, "OR", "AND", "LPAREN", "RPAREN", "IN", "NUM", "CATEGORY", "NAME", 
		"RATING", "PRICE", "STRING", "WHITESPACE", "INEQ"
	};
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}


	public QueryLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "Query.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getChannelNames() { return channelNames; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2\17^\b\1\4\2\t\2\4"+
		"\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t"+
		"\13\4\f\t\f\4\r\t\r\4\16\t\16\3\2\3\2\3\2\3\3\3\3\3\3\3\4\3\4\3\5\3\5"+
		"\3\6\3\6\3\6\3\7\3\7\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\t\3\t\3\t\3"+
		"\t\3\t\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\13\3\13\3\13\3\13\3\13\3\13\3\f\3"+
		"\f\7\fJ\n\f\f\f\16\fM\13\f\3\r\6\rP\n\r\r\r\16\rQ\3\r\3\r\3\16\3\16\3"+
		"\16\3\16\3\16\3\16\3\16\5\16]\n\16\2\2\17\3\3\5\4\7\5\t\6\13\7\r\b\17"+
		"\t\21\n\23\13\25\f\27\r\31\16\33\17\3\2\5\5\2C\\aac|\6\2\62;C\\aac|\5"+
		"\2\13\f\17\17\"\"\2c\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2\t\3\2\2\2\2"+
		"\13\3\2\2\2\2\r\3\2\2\2\2\17\3\2\2\2\2\21\3\2\2\2\2\23\3\2\2\2\2\25\3"+
		"\2\2\2\2\27\3\2\2\2\2\31\3\2\2\2\2\33\3\2\2\2\3\35\3\2\2\2\5 \3\2\2\2"+
		"\7#\3\2\2\2\t%\3\2\2\2\13\'\3\2\2\2\r*\3\2\2\2\17,\3\2\2\2\21\65\3\2\2"+
		"\2\23:\3\2\2\2\25A\3\2\2\2\27G\3\2\2\2\31O\3\2\2\2\33\\\3\2\2\2\35\36"+
		"\7~\2\2\36\37\7~\2\2\37\4\3\2\2\2 !\7(\2\2!\"\7(\2\2\"\6\3\2\2\2#$\7*"+
		"\2\2$\b\3\2\2\2%&\7+\2\2&\n\3\2\2\2\'(\7k\2\2()\7p\2\2)\f\3\2\2\2*+\4"+
		"\63\67\2+\16\3\2\2\2,-\7e\2\2-.\7c\2\2./\7v\2\2/\60\7g\2\2\60\61\7i\2"+
		"\2\61\62\7q\2\2\62\63\7t\2\2\63\64\7{\2\2\64\20\3\2\2\2\65\66\7p\2\2\66"+
		"\67\7c\2\2\678\7o\2\289\7g\2\29\22\3\2\2\2:;\7t\2\2;<\7c\2\2<=\7v\2\2"+
		"=>\7k\2\2>?\7p\2\2?@\7i\2\2@\24\3\2\2\2AB\7r\2\2BC\7t\2\2CD\7k\2\2DE\7"+
		"e\2\2EF\7g\2\2F\26\3\2\2\2GK\t\2\2\2HJ\t\3\2\2IH\3\2\2\2JM\3\2\2\2KI\3"+
		"\2\2\2KL\3\2\2\2L\30\3\2\2\2MK\3\2\2\2NP\t\4\2\2ON\3\2\2\2PQ\3\2\2\2Q"+
		"O\3\2\2\2QR\3\2\2\2RS\3\2\2\2ST\b\r\2\2T\32\3\2\2\2U]\7@\2\2VW\7@\2\2"+
		"W]\7?\2\2X]\7>\2\2YZ\7>\2\2Z]\7?\2\2[]\7?\2\2\\U\3\2\2\2\\V\3\2\2\2\\"+
		"X\3\2\2\2\\Y\3\2\2\2\\[\3\2\2\2]\34\3\2\2\2\6\2KQ\\\3\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}
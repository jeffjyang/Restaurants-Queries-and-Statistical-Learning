grammar Query;

@header {
package ca.ece.ubc.cpen221.mp5.query;
}

OR : '||';
AND : '&&';
LPAREN : '(';
RPAREN : ')';
IN : 'in';
NUM :  ([1-5] | '\u002E')+;
CATEGORY : 'category';
NAME : 'name';
RATING : 'rating';
PRICE : 'price';
STRING   : ('a'..'z' | 'A'..'Z' | '_' | '\u0027' | '-') ('a'..'z' | 'A'..'Z' | '_' | '0'..'9' |'\u0027' | '-')*;
WHITESPACE : [ \t\r\n]+ -> skip;
INEQ : ('>' | '>=' | '<' | '<=' | '=');

root : orexpr | andexpr | atom EOF;
orexpr : atom(OR atom)*;
andexpr : atom(AND atom)*;
atom : in | category | rating | price | name | LPAREN orexpr RPAREN;
in : IN LPAREN STRING RPAREN;
category : CATEGORY LPAREN STRING RPAREN;
name : NAME LPAREN STRING RPAREN;
price : PRICE INEQ NUM;
rating : RATING INEQ NUM;

 
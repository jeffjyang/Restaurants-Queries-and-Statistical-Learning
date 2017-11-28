grammar Query;

@header {
package ca.ece.ubc.cpen221.mp5.query;
}

OR : '||';
AND : '&&';
LPAREN : '(';
RPAREN : ')';
IN : 'in';
GT : '>';
GTE : '>=';
LT : '<';
LTE : '<=';
EQ : '=';
NUM :  '1'..'5';
CATEGORY : 'category';
NAME : 'name';
RATING : 'rating';
PRICE : 'price';
STRING : ('a'..'z' | 'A'..'Z')+;
WHITESPACE : [ \t\r\n]+ -> skip;
INEQ : ('>' | '>=' | '<' | '<=' | '=');

root : orexpr | andexpr | atom EOF;
orexpr : andexpr(OR andexpr)*;
andexpr : atom(AND atom)*;
atom : in | category | rating | price | name | LPAREN orexpr RPAREN;
in : IN LPAREN STRING RPAREN;
category : CATEGORY LPAREN STRING RPAREN;
name : NAME LPAREN STRING RPAREN;
price : PRICE INEQ NUM;
rating : RATING INEQ NUM;


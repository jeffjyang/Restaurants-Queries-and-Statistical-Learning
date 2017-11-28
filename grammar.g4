grammar Query;

OR : '||';
AND : '&&';
LPAREN : '(';
RPARENT : ')';
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
WHITESPACE : (' ' | '\t')+;

ATOM : in | category | rating | price | name | LParen orExpr RParen;
INEQ : gt | gte | lt | lte | eq;
in : IN LPAREN STRING RPAREN;
category : CATEGORY LPAREN STRING RPARREN;
name : NAME LPAREN STRING RPAREN;
price : PRICE INEQ NUM;


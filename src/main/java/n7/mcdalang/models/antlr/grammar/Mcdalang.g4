/*------ Grammaire du pseudo-langage Mcdalang ------*/

grammar Mcdalang;

// Point d'entrée pour le parseur
prog
    : statement+
    ;

// Instructions
statement
    : methodDecl
    | varDecl NEWLINE
    | assignment NEWLINE
    | incrStmt NEWLINE
    | returnStmt NEWLINE
    | printStmt NEWLINE
    | funcCall NEWLINE?
    | expr NEWLINE
    | ifStmt
    | loopStmt
    | block
    | NEWLINE
    ;

// Incrémentation / Décrémentation (instruction)
incrStmt
    : ID '++'
    | ID '--'
    ;

// Déclaration de méthode avec paramètres
methodDecl
    : 'methode' type ID '(' paramList? ')' block
    ;

// Paramètres d’une méthode
paramList
    : type ID (',' type ID)*
    ;

// Déclaration de variable/constante
varDecl
    : ('var' | 'const') type ID ( '=' expr )?
    ;

// Types de base
type
    : 'entier'
    | 'flottant'
    | 'chaine'
    | 'bool'
    | 'char'
    | 'vide'
    ;

// Assignation
assignment
    : ID '=' expr
    ;

// Retour de fonction
returnStmt
    : 'return' expr
    ;

// Affichage
printStmt
    : 'afficher' '(' expr ')'
    ;

// Appel de fonction
funcCall
    : (ID '.')* ID '(' (expr (',' expr)*)? ')'
    ;

// Conditions
ifStmt
    : 'si' '(' expr ')' block ('sinon' block)?
    | 'si' '(' expr ')' block 'snsi' '(' expr ')' block ('sinon' block)?
    ;

// Boucles
loopStmt
    : 'tantque' '(' expr ')' block
    | 'faire' block 'tantque' '(' expr ')'
    | 'pour' '(' assignment ';' expr ';' assignment ')' block
    ;

// Bloc
block
    : '{' statement* '}'
    ;

// Expression avec ordre de précédence
expr
    : orExpr ('?' expr ':' expr)?
    ;

orExpr
    : andExpr ( ( '||' | 'OR' ) andExpr )*
    ;

andExpr
    : notExpr ( ( '&&' | 'AND' ) notExpr )*
    ;

notExpr
    : ('!' | 'NOT') notExpr      // support both ! and NOT
    | concatenationExpr
    ;


concatenationExpr
    : equalityExpr ( '&' equalityExpr )*
    ;

equalityExpr
    : relationalExpr ( ( '==' | '!=' ) relationalExpr )*
    ;

relationalExpr
    : addExpr ( ( '>' | '<' | '>=' | '<=' ) addExpr )*
    ;

addExpr
    : mulExpr ( ( '+' | '-' ) mulExpr )*
    ;

mulExpr
    : powExpr ( ( '*' | '/' | '//' | '%' ) powExpr )*
    ;

powExpr
    : atom ( '^' atom )*
    ;

atom
    : INT
    | FLOAT
    | STRING
    | CHAR
    | 'true'
    | 'false'
    | ID
    | funcCall
    | '(' expr ')'
    ;

// Lexèmes
ID      : [a-zA-Z_][a-zA-Z_0-9]* ;
FLOAT   : [0-9]+ '.' [0-9]+ ;
INT     : [0-9]+ ;
STRING  : '"' (~["\\] | '\\' .)* '"' ;
CHAR    : '\'' . '\'' ;

// Espaces et commentaires
NEWLINE     : '\r'? '\n' ;
WS          : [ \t]+ -> skip ;
COMMENT_ML  : '/**' .*? '*/' -> skip ;
COMMENT_SL  : '#' ~[\r\n]* -> skip;
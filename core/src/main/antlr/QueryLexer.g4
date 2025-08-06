lexer grammar QueryLexer;

// Fragments
fragment OCTAL_DIGIT: [0-7];
fragment HEX_DIGIT: [0-9a-fA-F];
fragment BINARY_DIGIT: [01];
fragment ALPHA: [a-zA-Z];
fragment DIGIT: [0-9];
fragment ALPHANUMERIC: ALPHA | DIGIT;
fragment WHITESPACE: [ \t\r\n\u000C]+;
fragment UNICODE_CHARACTER: '\\' 'u' HEX_DIGIT HEX_DIGIT HEX_DIGIT HEX_DIGIT;
fragment ESC_SEQUENCE: '\\' [btnfr"'\\] | UNICODE_CHARACTER;

// Conditional Operators
EQUALS: '==';
NOT_EQUALS: '!=';
LESS_THAN: '<';
GREATER_THAN: '>';
LESS_THAN_OR_EQUAL: '<=';
GREATER_THAN_OR_EQUAL: '>=';

// Punctuation
LPARENTHESES: '(';
RPARENTHESES: ')';
COMMA: ',';
DOT: '.';

// Arithmetic Operators
EXPONENTIATION: '^';
PLUS: '+';
MINUS: '-';
MULTIPLY: '*';
DIVIDE: '/';
MODULO: '%';

// Logical Operators
AND: 'and';
OR: 'or';
NOT: 'not';

// Membership Operators
IN: 'in';
NOT_IN: 'not' WS* 'in';

// Boolean Literals
TRUE: 'true';
FALSE: 'false';

// Null Literal
NULL: 'null';

// String Literals (Single-quoted)
SINGLE_QUOTED_STRING_LITERAL: '\'' (ESC_SEQUENCE | ~['\\\r\n])* '\'' ;

// String Literals (Double-quoted)
DOUBLE_QUOTED_STRING_LITERAL: '"' (ESC_SEQUENCE | ~["\\\r\n])* '"';

// Integer Literals
OCTAL_INTEGER_LITERAL: '0o' OCTAL_DIGIT+;
HEX_INTEGER_LITERAL: '0x' HEX_DIGIT+;
BINARY_INTEGER_LITERAL: '0b' BINARY_DIGIT+;

// Numeric Literals
NUMERIC_LITERAL: DIGIT+ ('.' DIGIT+)?;

// Identifiers
IDENTIFIER: ALPHA (ALPHANUMERIC | '_')*;

// Whitespace
WS: WHITESPACE -> skip;

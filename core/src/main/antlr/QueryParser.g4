parser grammar QueryParser;

options {
    tokenVocab = QueryLexer;
}

expression:
    // Parentheses for grouping
    LPARENTHESES expression RPARENTHESES # SubExpression

    // Function call
    | identifier = IDENTIFIER LPARENTHESES (arguments += expression (COMMA arguments += expression)*)? RPARENTHESES # FunctionCall

    // Object Access
    | base = expression DOT member = IDENTIFIER # ObjectAccess

    // Unary operators
    | PLUS operand = expression  # UnaryPlus
    | MINUS operand = expression # UnaryMinus
    | NOT operand = expression   # Not

    // Arithmetic operators
    | <assoc = right> left = expression EXPONENTIATION right = expression # Exponentiation
    | left = expression MULTIPLY right = expression                       # Multiplication
    | left = expression DIVIDE right = expression                         # Division
    | left = expression MODULO right = expression                         # Modulo
    | left = expression PLUS right = expression                           # Addition
    | left = expression MINUS right = expression                          # Subtraction

    // Relational operators
    | left = expression LESS_THAN right = expression             # LessThan
    | left = expression GREATER_THAN right = expression          # GreaterThan
    | left = expression LESS_THAN_OR_EQUAL right = expression    # LessThanOrEqual
    | left = expression GREATER_THAN_OR_EQUAL right = expression # GreaterThanOrEqual

    // Equality operators
    | left = expression EQUALS right = expression     # Equals
    | left = expression NOT_EQUALS right = expression # NotEquals

    // Membership operator
    | item = expression IN LPARENTHESES elements += expression (COMMA elements += expression)* RPARENTHESES # In
    | item = expression NOT_IN LPARENTHESES elements += expression (COMMA elements += expression)* RPARENTHESES # NotIn

    // Logical operators
    | left = expression AND right = expression # And
    | left = expression OR right = expression  # Or

    // Literals
    | OCTAL_INTEGER_LITERAL        # OctalIntegerLiteral
    | HEX_INTEGER_LITERAL          # HexIntegerLiteral
    | BINARY_INTEGER_LITERAL       # BinaryIntegerLiteral
    | NUMERIC_LITERAL              # NumericLiteral
    | SINGLE_QUOTED_STRING_LITERAL # SingleQuotedStringLiteral
    | DOUBLE_QUOTED_STRING_LITERAL # DoubleQuotedStringLiteral
    | TRUE                         # TrueLiteral
    | FALSE                        # FalseLiteral
    | NULL                         # NullLiteral

    // Identifiers
    | IDENTIFIER # Identifier;

package com.srilakshmikanthanp.searchql.core.ast

interface NodeVisitor<T> {
  fun visitUnaryPlus(expression: UnaryPlusNode): T
  fun visitMultiplication(node: MultiplicationNode): T
  fun visitStringLiteral(node: StringLiteralNode): T
  fun visitBooleanLiteral(node: BooleanLiteralNode): T
  fun visitOr(node: OrExpressionNode): T
  fun visitLessThan(node: LessThanNode): T
  fun visitNull(node: NullExpressionNode): T
  fun visitNumericLiteral(literal: NumericLiteralNode): T
  fun visitGreaterThan(node: GreaterThanNode): T
  fun visitIntegerLiteral(node: IntegerLiteralNode): T
  fun visitExponentiation(node: ExponentiationNode): T
  fun visitIdentifier(expression: IdentifierNode): T
  fun visitDivision(node: DivisionNode): T
  fun visitObjectAccess(node: ObjectAccessNode): T
  fun visitAdditionNode(node: AdditionNode): T
  fun visitUnaryMinus(node: UnaryMinusNode): T
  fun visitNot(node: NotNode): T
  fun visitEquals(node: EqualsNode): T
  fun visitNotEquals(node: NotEqualsNode): T
  fun visitSubtraction(node: SubtractionNode): T
  fun visitAnd(node: AndNode): T
  fun visitModulo(node: ModuloNode): T
  fun visitIn(node: InNode): T
  fun visitNotIn(node: NotInNode): T
  fun visitFunctionCall(node: FunctionCallNode): T
}

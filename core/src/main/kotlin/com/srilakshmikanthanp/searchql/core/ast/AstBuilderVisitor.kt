package com.srilakshmikanthanp.searchql.core.ast

import com.srilakshmikanthanp.searchql.core.grammar.QueryParser
import com.srilakshmikanthanp.searchql.core.grammar.QueryParserBaseVisitor

class AstBuilderVisitor : QueryParserBaseVisitor<Node>() {
  override fun visitMultiplication(ctx: QueryParser.MultiplicationContext): Node {
    return MultiplicationNode(visit(ctx.left).asExpressionNode(), visit(ctx.right).asExpressionNode())
  }

  override fun visitOr(ctx: QueryParser.OrContext): Node {
    return OrExpressionNode(visit(ctx.left).asExpressionNode(), visit(ctx.right).asExpressionNode())
  }

  override fun visitNumericLiteral(ctx: QueryParser.NumericLiteralContext): Node {
    return NumericLiteralNode(ctx.text.toDouble())
  }

  override fun visitLessThanOrEqual(ctx: QueryParser.LessThanOrEqualContext): Node {
    return LessThanNode(visit(ctx.left).asExpressionNode(), visit(ctx.right).asExpressionNode())
  }

  override fun visitIn(ctx: QueryParser.InContext?): Node {
    return InNode(ctx!!.item.accept(this).asExpressionNode(), ctx.elements.map { it.accept(this).asExpressionNode() })
  }

  override fun visitUnaryPlus(ctx: QueryParser.UnaryPlusContext): Node {
    return UnaryPlusNode(visit(ctx.operand).asExpressionNode())
  }

  override fun visitNotIn(ctx: QueryParser.NotInContext): Node {
    return NotInNode(ctx.item.accept(this).asExpressionNode(), ctx.elements.map { it.accept(this).asExpressionNode() })
  }

  override fun visitExponentiation(ctx: QueryParser.ExponentiationContext): Node {
    return ExponentiationNode(visit(ctx.left).asExpressionNode(), visit(ctx.right).asExpressionNode())
  }

  override fun visitGreaterThanOrEqual(ctx: QueryParser.GreaterThanOrEqualContext): Node {
    return GreaterThanNode(visit(ctx.left).asExpressionNode(), visit(ctx.right).asExpressionNode())
  }

  override fun visitFalseLiteral(ctx: QueryParser.FalseLiteralContext?): Node {
    return BooleanLiteralNode(false)
  }

  override fun visitSingleQuotedStringLiteral(ctx: QueryParser.SingleQuotedStringLiteralContext): Node {
    return StringLiteralNode(ctx.text.removeSurrounding(SINGLE_QUOTE, SINGLE_QUOTE))
  }

  override fun visitLessThan(ctx: QueryParser.LessThanContext): Node {
    return LessThanNode(visit(ctx.left).asExpressionNode(), visit(ctx.right).asExpressionNode())
  }

  override fun visitBinaryIntegerLiteral(ctx: QueryParser.BinaryIntegerLiteralContext): Node {
    return IntegerLiteralNode(ctx.text.toLong(2))
  }

  override fun visitIdentifier(ctx: QueryParser.IdentifierContext): Node {
    return IdentifierNode(ctx.text)
  }

  override fun visitTrueLiteral(ctx: QueryParser.TrueLiteralContext?): Node {
    return BooleanLiteralNode(true)
  }

  override fun visitGreaterThan(ctx: QueryParser.GreaterThanContext): Node {
    return GreaterThanNode(visit(ctx.left).asExpressionNode(), visit(ctx.right).asExpressionNode())
  }

  override fun visitObjectAccess(ctx: QueryParser.ObjectAccessContext): Node {
    return ObjectAccessNode(visit(ctx.base).asExpressionNode(), ctx.member.text)
  }

  override fun visitDivision(ctx: QueryParser.DivisionContext): Node {
    return DivisionNode(visit(ctx.left).asExpressionNode(), visit(ctx.right).asExpressionNode())
  }

  override fun visitFunctionCall(ctx: QueryParser.FunctionCallContext): Node {
    return FunctionCallNode(ctx.identifier.text, ctx.arguments.map { it.accept(this).asExpressionNode() })
  }

  override fun visitSubExpression(ctx: QueryParser.SubExpressionContext): Node {
    return ctx.expression().accept(this)
  }

  override fun visitOctalIntegerLiteral(ctx: QueryParser.OctalIntegerLiteralContext): Node {
    return IntegerLiteralNode(ctx.text.toLong(8))
  }

  override fun visitAddition(ctx: QueryParser.AdditionContext): Node {
    return AdditionNode(visit(ctx.left).asExpressionNode(), visit(ctx.right).asExpressionNode())
  }

  override fun visitModulo(ctx: QueryParser.ModuloContext): Node {
    return ModuloNode(visit(ctx.left).asExpressionNode(), visit(ctx.right).asExpressionNode())
  }

  override fun visitUnaryMinus(ctx: QueryParser.UnaryMinusContext): Node {
    return UnaryMinusNode(visit(ctx.operand).asExpressionNode())
  }

  override fun visitNotEquals(ctx: QueryParser.NotEqualsContext): Node {
    return NotEqualsNode(visit(ctx.left).asExpressionNode(), visit(ctx.right).asExpressionNode())
  }

  override fun visitHexIntegerLiteral(ctx: QueryParser.HexIntegerLiteralContext): Node {
    return IntegerLiteralNode(ctx.text.toLong(16))
  }

  override fun visitNot(ctx: QueryParser.NotContext): Node {
    return NotNode(visit(ctx.expression()).asExpressionNode())
  }

  override fun visitEquals(ctx: QueryParser.EqualsContext?): Node {
    return EqualsNode(visit(ctx!!.left).asExpressionNode(), visit(ctx.right).asExpressionNode())
  }

  override fun visitSubtraction(ctx: QueryParser.SubtractionContext?): Node {
    return SubtractionNode(visit(ctx!!.left).asExpressionNode(), visit(ctx.right).asExpressionNode())
  }

  override fun visitAnd(ctx: QueryParser.AndContext): Node {
    return AndNode(visit(ctx.left).asExpressionNode(), visit(ctx.right).asExpressionNode())
  }

  override fun visitDoubleQuotedStringLiteral(ctx: QueryParser.DoubleQuotedStringLiteralContext): Node {
    return StringLiteralNode(ctx.text.removeSurrounding(DOUBLE_QUOTE, DOUBLE_QUOTE))
  }

  override fun visitNullLiteral(ctx: QueryParser.NullLiteralContext): Node {
    return NullExpressionNode()
  }

  companion object {
    private const val SINGLE_QUOTE = "'"
    private const val DOUBLE_QUOTE = "\""
  }
}

package com.srilakshmikanthanp.searchql.core.ast

class NumericLiteralNode(override val literal: Double) : LiteralExpressionNode(literal) {
  override fun <T> accept(visitor: NodeVisitor<T>): T {
    return visitor.visitNumericLiteral(this)
  }
}
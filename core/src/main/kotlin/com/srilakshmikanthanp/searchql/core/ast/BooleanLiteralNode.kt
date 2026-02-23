package com.srilakshmikanthanp.searchql.core.ast

class BooleanLiteralNode(override val literal: Boolean) : LiteralExpressionNode(literal) {
  override fun <T> accept(visitor: NodeVisitor<T>): T {
    return visitor.visitBooleanLiteral(this)
  }
}

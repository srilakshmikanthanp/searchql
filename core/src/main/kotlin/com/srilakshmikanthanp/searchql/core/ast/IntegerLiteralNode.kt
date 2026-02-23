package com.srilakshmikanthanp.searchql.core.ast

class IntegerLiteralNode(override val literal: Long) : LiteralExpressionNode(literal) {
  override fun <T> accept(visitor: NodeVisitor<T>): T {
    return visitor.visitIntegerLiteral(this)
  }
}

package com.srilakshmikanthanp.searchql.core.ast

class StringLiteralNode(override val literal: String) : LiteralExpressionNode(literal) {
  override fun <T> accept(visitor: NodeVisitor<T>): T {
    return visitor.visitStringLiteral(this)
  }
}
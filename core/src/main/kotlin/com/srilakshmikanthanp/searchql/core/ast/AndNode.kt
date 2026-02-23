package com.srilakshmikanthanp.searchql.core.ast

class AndNode(
  left: ExpressionNode,
  right: ExpressionNode
) : BinaryExpressionNode(left, right) {
  override fun <T> accept(visitor: NodeVisitor<T>): T {
    return visitor.visitAnd(this)
  }
}

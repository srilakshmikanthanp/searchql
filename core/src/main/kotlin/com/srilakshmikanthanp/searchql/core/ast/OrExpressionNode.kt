package com.srilakshmikanthanp.searchql.core.ast

class OrExpressionNode(
  left: ExpressionNode,
  right: ExpressionNode
) : BinaryExpressionNode(left, right) {
  override fun <T> accept(visitor: NodeVisitor<T>): T {
    return visitor.visitOr(this)
  }
}

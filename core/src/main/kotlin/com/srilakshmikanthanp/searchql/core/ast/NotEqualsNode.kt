package com.srilakshmikanthanp.searchql.core.ast

class NotEqualsNode(
  override val left: ExpressionNode,
  override val right: ExpressionNode
): BinaryExpressionNode(left, right) {
  override fun <T> accept(visitor: NodeVisitor<T>): T {
    return visitor.visitNotEquals(this)
  }
}

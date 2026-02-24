package com.srilakshmikanthanp.searchql.core.ast

class LessThanNode(
  override val left: ExpressionNode,
  override val right: ExpressionNode
) : BinaryExpressionNode(left, right) {
  override fun <T> accept(visitor: NodeVisitor<T>): T {
    return visitor.visitLessThan(this)
  }
}

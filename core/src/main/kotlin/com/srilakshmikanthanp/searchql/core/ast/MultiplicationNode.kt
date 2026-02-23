package com.srilakshmikanthanp.searchql.core.ast

class MultiplicationNode(
  left: ExpressionNode,
  right: ExpressionNode
) : BinaryExpressionNode(left, right) {
  override fun <T> accept(visitor: NodeVisitor<T>): T {
    return visitor.visitMultiplication(this)
  }
}

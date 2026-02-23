package com.srilakshmikanthanp.searchql.core.ast

sealed class ComparisonNode(
  override val left: ExpressionNode,
  override val right: ExpressionNode
) : BinaryExpressionNode(left, right) {
  override fun <T> accept(visitor: NodeVisitor<T>): T {
    throw UnsupportedOperationException("ComparisonExpressionNode is an abstract class and should not be instantiated directly.")
  }
}

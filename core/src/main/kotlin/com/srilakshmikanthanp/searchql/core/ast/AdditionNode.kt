package com.srilakshmikanthanp.searchql.core.ast

class AdditionNode(
  val left: ExpressionNode,
  val right: ExpressionNode
) : ExpressionNode {
  override fun <T> accept(visitor: NodeVisitor<T>): T {
    return visitor.visitAdditionNode(this)
  }
}

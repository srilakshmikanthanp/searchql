package com.srilakshmikanthanp.searchql.core.ast

class SubtractionNode(
  val left: ExpressionNode,
  val right: ExpressionNode
) : ExpressionNode {
  override fun <T> accept(visitor: NodeVisitor<T>): T {
    return visitor.visitSubtraction(this)
  }
}

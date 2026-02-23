package com.srilakshmikanthanp.searchql.core.ast

class FunctionCallNode(
  val identifier: String,
  val arguments: List<ExpressionNode>
) : ExpressionNode {
  override fun <T> accept(visitor: NodeVisitor<T>): T {
    return visitor.visitFunctionCall(this)
  }
}

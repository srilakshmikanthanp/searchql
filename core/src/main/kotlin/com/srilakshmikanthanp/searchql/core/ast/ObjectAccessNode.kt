package com.srilakshmikanthanp.searchql.core.ast

class ObjectAccessNode(
  val base: ExpressionNode,
  val member: String
) : ExpressionNode {
  override fun <T> accept(visitor: NodeVisitor<T>): T {
    return visitor.visitObjectAccess(this)
  }
}

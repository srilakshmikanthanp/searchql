package com.srilakshmikanthanp.searchql.core.ast

class InNode(
  member: ExpressionNode,
  elements: List<ExpressionNode>
) : MembershipNode(member, elements) {
  override fun <T> accept(visitor: NodeVisitor<T>): T {
    return visitor.visitIn(this)
  }
}

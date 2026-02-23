package com.srilakshmikanthanp.searchql.core.ast

class NotNode(override val operand: ExpressionNode) : UnaryExpressionNode(operand) {
  override fun <T> accept(visitor: NodeVisitor<T>): T {
    return visitor.visitNot(this)
  }
}

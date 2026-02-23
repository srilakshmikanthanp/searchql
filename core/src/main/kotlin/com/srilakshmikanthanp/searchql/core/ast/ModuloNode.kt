package com.srilakshmikanthanp.searchql.core.ast

class ModuloNode(
  override val left: ExpressionNode,
  override val right: ExpressionNode
) : BinaryExpressionNode(left, right) {
  override fun <T> accept(visitor: NodeVisitor<T>): T {
    return visitor.visitModulo(this)
  }
}

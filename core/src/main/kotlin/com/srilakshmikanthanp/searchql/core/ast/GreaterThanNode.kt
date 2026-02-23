package com.srilakshmikanthanp.searchql.core.ast

class GreaterThanNode(override val left: Node, override val right: Node) : BinaryExpressionNode(left, right) {
  override fun <T> accept(visitor: NodeVisitor<T>): T {
    return visitor.visitGreaterThan(this)
  }
}

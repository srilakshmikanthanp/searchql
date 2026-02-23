package com.srilakshmikanthanp.searchql.core.ast

class IdentifierNode(val name: String) : ExpressionNode {
  override fun <T> accept(visitor: NodeVisitor<T>): T {
    return visitor.visitIdentifier(this)
  }
}

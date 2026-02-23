package com.srilakshmikanthanp.searchql.core.ast

class NullExpressionNode : ExpressionNode {
  override fun <T> accept(visitor: NodeVisitor<T>): T {
    return visitor.visitNull(this)
  }
}

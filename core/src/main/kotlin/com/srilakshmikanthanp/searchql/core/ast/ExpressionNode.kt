package com.srilakshmikanthanp.searchql.core.ast

interface ExpressionNode : Node

fun Node.asExpressionNode(): ExpressionNode {
  if (this !is ExpressionNode) {
    throw NodeMismatchException(expectedTypes = arrayOf(ExpressionNode::class.java), actualType = this::class.java)
  } else {
    return this
  }
}

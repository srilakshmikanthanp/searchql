package com.srilakshmikanthanp.searchql.jpa.transform

import jakarta.persistence.criteria.Expression

class JpaExpressionDoubleNode(
  override val expression: Expression<Double>
): JpaExpressionNumberNode<Double>(expression)

fun JpaNode.asExpressionDoubleNode(): JpaExpressionDoubleNode {
  if (this !is JpaExpressionDoubleNode) {
    throw JpaNodeMismatchException(
      expectedTypes = arrayOf(JpaExpressionDoubleNode::class.java),
      actualType = this::class.java
    )
  } else {
    return this
  }
}

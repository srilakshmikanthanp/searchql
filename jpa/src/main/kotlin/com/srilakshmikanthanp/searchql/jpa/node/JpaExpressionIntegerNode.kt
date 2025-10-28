package com.srilakshmikanthanp.searchql.jpa.node

import jakarta.persistence.criteria.CriteriaBuilder
import jakarta.persistence.criteria.Expression

class JpaExpressionIntegerNode(
  override val expression: Expression<Int>
): JpaExpressionNumberNode<Int>(expression), JpaModuloNode {
  override fun modulo(
    cb: CriteriaBuilder,
    other: JpaNode
  ): JpaNode {
    cb.mod(
      this.expression,
      other.asExpressionIntegerNode().expression
    ).let {
      return JpaExpressionIntegerNode(it)
    }
  }
}

fun JpaNode.asExpressionIntegerNode(): JpaExpressionIntegerNode {
  if (this !is JpaExpressionIntegerNode) {
    throw JpaNodeMismatchException(
      expectedTypes = arrayOf(JpaExpressionIntegerNode::class.java),
      actualType = this::class.java
    )
  } else {
    return this
  }
}

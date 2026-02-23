package com.srilakshmikanthanp.searchql.jpa.transform

import jakarta.persistence.criteria.CriteriaBuilder
import jakarta.persistence.criteria.Expression

open class JpaExpressionStringNode(
  override val expression: Expression<String>
): JpaExpressionNode<String>(expression), JpaAddableNode {
  override fun add(
    cb: CriteriaBuilder,
    other: JpaNode
  ): JpaNode {
    val rightExpression = other.asExpressionStringNode().expression
    val result = cb.concat(this.expression, rightExpression)
    return JpaExpressionStringNode(result)
  }
}

fun JpaNode.asExpressionStringNode(): JpaExpressionStringNode {
  if (this !is JpaExpressionStringNode) {
    throw JpaNodeMismatchException(expectedTypes = arrayOf(JpaExpressionNode::class.java), actualType = this::class.java)
  } else {
    return this
  }
}

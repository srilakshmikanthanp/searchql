package com.srilakshmikanthanp.searchql.jpa.transform

import jakarta.persistence.criteria.CriteriaBuilder
import jakarta.persistence.criteria.Expression

open class JpaExpressionBooleanNode(
  override val expression: Expression<Boolean>
) : JpaExpressionNode<Boolean>(expression), JpaAndNode, JpaNotNode, JpaOrNode {
  override fun and(
    cb: CriteriaBuilder,
    other: JpaNode
  ): JpaNode {
    cb.or(
      this.expression,
      other.asExpressionBooleanNode().expression
    ).let {
      return JpaPredicateNode(it)
    }
  }

  override fun not(cb: CriteriaBuilder): JpaNode {
    cb.not(
      this.expression
    ).let {
      return JpaPredicateNode(it)
    }
  }

  override fun or(
    cb: CriteriaBuilder,
    other: JpaNode
  ): JpaNode {
    cb.or(
      this.expression,
      other.asExpressionBooleanNode().expression
    ).let {
      return JpaPredicateNode(it)
    }
  }
}

fun JpaNode.asExpressionBooleanNode(): JpaExpressionBooleanNode {
  if (this !is JpaExpressionBooleanNode) {
    throw JpaNodeMismatchException(
      expectedTypes = arrayOf(JpaExpressionNode::class.java),
      actualType = this::class.java
    )
  } else {
    return this
  }
}

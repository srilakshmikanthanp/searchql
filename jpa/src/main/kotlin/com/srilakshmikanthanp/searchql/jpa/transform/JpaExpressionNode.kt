package com.srilakshmikanthanp.searchql.jpa.transform

import jakarta.persistence.criteria.CriteriaBuilder
import jakarta.persistence.criteria.Expression

open class JpaExpressionNode<T>(open val expression: Expression<T>): JpaNode, JpaEqNode, JpaNotEqNode {
  override fun eq(
    cb: CriteriaBuilder,
    other: JpaNode
  ): JpaNode {
    cb.equal(
      this.expression,
      other.asExpressionNode().expression
    ).let {
      return JpaPredicateNode(it)
    }
  }

  override fun notEq(
    cb: CriteriaBuilder,
    other: JpaNode
  ): JpaNode {
    cb.notEqual(
      this.expression,
      other.asExpressionNode().expression
    ).let {
      return JpaPredicateNode(it)
    }
  }
}

fun JpaNode.asExpressionNode(): JpaExpressionNode<*> {
  if (this !is JpaExpressionNode<*>) {
    throw JpaNodeMismatchException(expectedTypes = arrayOf(JpaExpressionNode::class.java), actualType = this::class.java)
  } else {
    return this
  }
}

inline fun <reified T> JpaExpressionNode<*>.asType(): JpaExpressionNode<T> {
  if (!this.isTypeOf(T::class.java)) {
    throw JpaExpressionNodeTypeMismatchException(expectedTypes = arrayOf(T::class.java), actualType = this.expression.javaType)
  } else {
    @Suppress("UNCHECKED_CAST")
    return this as JpaExpressionNode<T>
  }
}

fun JpaExpressionNode<*>.isTypeOf(vararg types: Class<*>): Boolean {
  return types.any { it.isAssignableFrom(this.expression.javaType) }
}

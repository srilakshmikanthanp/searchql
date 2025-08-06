package com.srilakshmikanthanp.searchql.jpa.node

import jakarta.persistence.criteria.Expression

open class JpaExpressionNode<T>(val expression: Expression<T>): JpaNode

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

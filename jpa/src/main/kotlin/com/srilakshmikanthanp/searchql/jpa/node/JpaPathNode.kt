package com.srilakshmikanthanp.searchql.jpa.node

import jakarta.persistence.criteria.Path

open class JpaPathNode<T>(
  val path: Path<T>
): JpaExpressionNode<T>(
  expression = path
)

fun JpaExpressionNode<*>.asPathNode(): JpaPathNode<*> {
  if (this !is JpaPathNode<*>) {
    throw JpaNodeMismatchException(expectedTypes = arrayOf(JpaPathNode::class.java), actualType = this::class.java)
  } else {
    return this
  }
}

inline fun <reified T> JpaPathNode<*>.asType(): JpaPathNode<T> {
  if (!this.isTypeOf(T::class.java)) {
    throw JpaPathNodeTypeMismatchException(expectedTypes = arrayOf(T::class.java), actualType = this.path.javaType)
  } else {
    @Suppress("UNCHECKED_CAST")
    return this as JpaPathNode<T>
  }
}

fun JpaPathNode<*>.isTypeOf(vararg types: Class<*>): Boolean {
  return types.any { it.isAssignableFrom(this.path.javaType) }
}

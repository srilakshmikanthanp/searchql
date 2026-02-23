package com.srilakshmikanthanp.searchql.jpa.transform

import jakarta.persistence.criteria.Predicate

class JpaPredicateNode(
  val predicate: Predicate
) : JpaExpressionBooleanNode(
  expression = predicate
)

fun JpaNode.asPredicateNode(): JpaPredicateNode {
  if (this !is JpaPredicateNode) {
    throw JpaNodeMismatchException(expectedTypes = arrayOf(JpaPredicateNode::class.java), actualType = this::class.java)
  } else {
    return this
  }
}

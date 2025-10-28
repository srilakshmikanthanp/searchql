package com.srilakshmikanthanp.searchql.jpa.node

import jakarta.persistence.criteria.CriteriaBuilder

interface JpaNotEqNode {
  fun notEq(cb: CriteriaBuilder, other: JpaNode): JpaNode
}

fun JpaNode.asNotEqNode(): JpaNotEqNode {
  if (this !is JpaNotEqNode) {
    throw JpaNodeMismatchException(expectedTypes = arrayOf(JpaNotEqNode::class.java), actualType = this::class.java)
  } else {
    return this
  }
}

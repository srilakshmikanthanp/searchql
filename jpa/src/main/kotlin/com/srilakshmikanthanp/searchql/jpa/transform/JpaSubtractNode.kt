package com.srilakshmikanthanp.searchql.jpa.transform

import jakarta.persistence.criteria.CriteriaBuilder

interface JpaSubtractNode {
  fun subtract(cb: CriteriaBuilder, other: JpaNode): JpaNode
}

fun JpaNode.asSubtractableNode(): JpaSubtractNode {
  if (this !is JpaSubtractNode) {
    throw JpaNodeMismatchException(expectedTypes = arrayOf(JpaSubtractNode::class.java), actualType = this::class.java)
  } else {
    return this
  }
}

package com.srilakshmikanthanp.searchql.jpa.node

import jakarta.persistence.criteria.CriteriaBuilder

interface JpaSubtractableNode {
  fun subtract(cb: CriteriaBuilder, other: JpaNode): JpaNode
}

fun JpaNode.asSubtractableNode(): JpaSubtractableNode {
  if (this !is JpaSubtractableNode) {
    throw JpaNodeMismatchException(expectedTypes = arrayOf(JpaSubtractableNode::class.java), actualType = this::class.java)
  } else {
    return this
  }
}

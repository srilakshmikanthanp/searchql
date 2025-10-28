package com.srilakshmikanthanp.searchql.jpa.node

import jakarta.persistence.criteria.CriteriaBuilder

interface JpaDividableNode {
  fun divide(cb: CriteriaBuilder, other: JpaNode): JpaNode
}

fun JpaNode.asDividableNode(): JpaDividableNode {
  if (this !is JpaDividableNode) {
    throw JpaNodeMismatchException(expectedTypes = arrayOf(JpaDividableNode::class.java), actualType = this::class.java)
  } else {
    return this
  }
}

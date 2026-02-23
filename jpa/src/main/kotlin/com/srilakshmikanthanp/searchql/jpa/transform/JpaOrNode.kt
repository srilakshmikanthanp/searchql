package com.srilakshmikanthanp.searchql.jpa.transform

import jakarta.persistence.criteria.CriteriaBuilder

interface JpaOrNode {
  fun or(cb: CriteriaBuilder, other: JpaNode): JpaNode
}

fun JpaNode.asOrNode(): JpaOrNode {
  if (this !is JpaOrNode) {
    throw JpaNodeMismatchException(expectedTypes = arrayOf(JpaOrNode::class.java), actualType = this::class.java)
  } else {
    return this
  }
}

package com.srilakshmikanthanp.searchql.jpa.transform

import jakarta.persistence.criteria.CriteriaBuilder

interface JpaPowerNode {
  fun power(cb: CriteriaBuilder, other: JpaNode): JpaNode
}

fun JpaNode.asPowerNode(): JpaPowerNode {
  if (this !is JpaPowerNode) {
    throw JpaNodeMismatchException(expectedTypes = arrayOf(JpaPowerNode::class.java), actualType = this::class.java)
  } else {
    return this
  }
}

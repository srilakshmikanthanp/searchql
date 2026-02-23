package com.srilakshmikanthanp.searchql.jpa.transform

import jakarta.persistence.criteria.CriteriaBuilder

interface JpaAndNode {
  fun and(cb: CriteriaBuilder, other: JpaNode): JpaNode
}

fun JpaNode.asAndNode(): JpaAndNode {
  if (this !is JpaAndNode) {
    throw JpaNodeMismatchException(expectedTypes = arrayOf(JpaAndNode::class.java), actualType = this::class.java)
  } else {
    return this
  }
}

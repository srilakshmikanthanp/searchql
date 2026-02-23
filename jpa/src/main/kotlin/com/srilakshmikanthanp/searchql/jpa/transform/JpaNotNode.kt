package com.srilakshmikanthanp.searchql.jpa.transform

import jakarta.persistence.criteria.CriteriaBuilder

interface JpaNotNode {
  fun not(cb: CriteriaBuilder): JpaNode
}

fun JpaNode.asNotNode(): JpaNotNode {
  if (this !is JpaNotNode) {
    throw JpaNodeMismatchException(expectedTypes = arrayOf(JpaNotNode::class.java), actualType = this::class.java)
  } else {
    return this
  }
}

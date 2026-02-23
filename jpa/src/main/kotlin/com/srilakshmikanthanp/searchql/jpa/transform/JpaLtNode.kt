package com.srilakshmikanthanp.searchql.jpa.transform

import jakarta.persistence.criteria.CriteriaBuilder

interface JpaLtNode {
  fun lt(cb: CriteriaBuilder, other: JpaNode): JpaNode
}

fun JpaNode.asLtNode(): JpaLtNode {
  if (this !is JpaLtNode) {
    throw JpaNodeMismatchException(expectedTypes = arrayOf(JpaLtNode::class.java), actualType = this::class.java)
  } else {
    return this
  }
}

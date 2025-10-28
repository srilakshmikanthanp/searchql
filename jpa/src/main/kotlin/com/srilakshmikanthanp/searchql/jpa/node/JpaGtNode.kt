package com.srilakshmikanthanp.searchql.jpa.node

import jakarta.persistence.criteria.CriteriaBuilder

interface JpaGtNode {
  fun gt(cb: CriteriaBuilder, other: JpaNode): JpaNode
}

fun JpaNode.asGtNode(): JpaGtNode {
  if (this !is JpaGtNode) {
    throw JpaNodeMismatchException(expectedTypes = arrayOf(JpaGtNode::class.java), actualType = this::class.java)
  } else {
    return this
  }
}

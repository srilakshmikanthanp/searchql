package com.srilakshmikanthanp.searchql.jpa.node

import jakarta.persistence.criteria.CriteriaBuilder

interface JpaAddableNode {
  fun add(cb: CriteriaBuilder, other: JpaNode): JpaNode
}

fun JpaNode.asAddableNode(): JpaAddableNode {
  if (this !is JpaAddableNode) {
    throw JpaNodeMismatchException(expectedTypes = arrayOf(JpaAddableNode::class.java), actualType = this::class.java)
  } else {
    return this
  }
}

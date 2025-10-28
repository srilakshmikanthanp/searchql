package com.srilakshmikanthanp.searchql.jpa.node

import jakarta.persistence.criteria.CriteriaBuilder

interface JpaLteNode {
  fun lte(cb: CriteriaBuilder, other: JpaNode): JpaNode
}

fun JpaNode.asLteNode(): JpaLteNode {
  if (this !is JpaLteNode) {
    throw JpaNodeMismatchException(expectedTypes = arrayOf(JpaLteNode::class.java), actualType = this::class.java)
  } else {
    return this
  }
}

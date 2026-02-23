package com.srilakshmikanthanp.searchql.jpa.transform

import jakarta.persistence.criteria.CriteriaBuilder

interface JpaGteNode {
  fun gte(cb: CriteriaBuilder, other: JpaNode): JpaNode
}

fun JpaNode.asGteNode(): JpaGteNode {
  if (this !is JpaGteNode) {
    throw JpaNodeMismatchException(expectedTypes = arrayOf(JpaGteNode::class.java), actualType = this::class.java)
  } else {
    return this
  }
}

package com.srilakshmikanthanp.searchql.jpa.node

import jakarta.persistence.criteria.CriteriaBuilder

interface JpaModuloNode {
  fun modulo(cb: CriteriaBuilder, other: JpaNode): JpaNode
}

fun JpaNode.asModuloNode(): JpaModuloNode {
  if (this !is JpaModuloNode) {
    throw JpaNodeMismatchException(expectedTypes = arrayOf(JpaModuloNode::class.java), actualType = this::class.java)
  } else {
    return this
  }
}

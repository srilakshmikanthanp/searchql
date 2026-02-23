package com.srilakshmikanthanp.searchql.jpa.transform

import jakarta.persistence.criteria.CriteriaBuilder

interface JpaMultipliableNode {
  fun multiply(cb: CriteriaBuilder, other: JpaNode): JpaNode
}

fun JpaNode.asMultipliableNode(): JpaMultipliableNode {
  if (this !is JpaMultipliableNode) {
    throw JpaNodeMismatchException(expectedTypes = arrayOf(JpaMultipliableNode::class.java), actualType = this::class.java)
  } else {
    return this
  }
}

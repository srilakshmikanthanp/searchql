package com.srilakshmikanthanp.searchql.jpa.node

import jakarta.persistence.criteria.CriteriaBuilder

interface JpsUnaryPlusNode {
  fun unaryPlus(cb: CriteriaBuilder): JpaNode
}

fun JpaNode.asUnaryPlusNode(): JpsUnaryPlusNode {
  if (this !is JpsUnaryPlusNode) {
    throw JpaNodeMismatchException(expectedTypes = arrayOf(JpsUnaryMinusNode::class.java), actualType = this::class.java)
  } else {
    return this
  }
}

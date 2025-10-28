package com.srilakshmikanthanp.searchql.jpa.node

import jakarta.persistence.criteria.CriteriaBuilder

interface JpsUnaryMinusNode {
  fun unaryMinus(cb: CriteriaBuilder): JpaNode
}

fun JpaNode.asUnaryMinusNode(): JpsUnaryMinusNode {
  if (this !is JpsUnaryMinusNode) {
    throw JpaNodeMismatchException(expectedTypes = arrayOf(JpsUnaryMinusNode::class.java), actualType = this::class.java)
  } else {
    return this
  }
}

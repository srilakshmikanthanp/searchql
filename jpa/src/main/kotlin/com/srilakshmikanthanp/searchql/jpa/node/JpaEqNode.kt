package com.srilakshmikanthanp.searchql.jpa.node

import jakarta.persistence.criteria.CriteriaBuilder

interface JpaEqNode {
  fun eq(cb: CriteriaBuilder, other: JpaNode): JpaNode
}

fun JpaNode.asEqNode(): JpaEqNode {
  if (this !is JpaEqNode) {
    throw JpaNodeMismatchException(expectedTypes = arrayOf(JpaEqNode::class.java), actualType = this::class.java)
  } else {
    return this
  }
}

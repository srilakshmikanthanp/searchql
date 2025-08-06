package com.srilakshmikanthanp.searchql.jpa.node

class JpaPredicateNodeException(
  message: String,
  cause: Throwable? = null
) : JpaNodeException(
  message,
  cause
)

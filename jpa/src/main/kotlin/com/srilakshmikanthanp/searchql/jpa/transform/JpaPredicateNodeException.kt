package com.srilakshmikanthanp.searchql.jpa.transform

class JpaPredicateNodeException(
  message: String,
  cause: Throwable? = null
) : JpaNodeException(
  message,
  cause
)

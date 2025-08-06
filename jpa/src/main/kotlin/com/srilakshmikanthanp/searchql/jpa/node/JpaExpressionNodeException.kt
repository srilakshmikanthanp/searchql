package com.srilakshmikanthanp.searchql.jpa.node

open class JpaExpressionNodeException(
  message: String = "Invalid expression node",
  cause: Throwable? = null
): JpaNodeException(message, cause)

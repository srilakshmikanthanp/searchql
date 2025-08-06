package com.srilakshmikanthanp.searchql.jpa.node

open class JpaNodeException(
  message: String = "Invalid JPA node",
  cause: Throwable? = null
): RuntimeException(message, cause)

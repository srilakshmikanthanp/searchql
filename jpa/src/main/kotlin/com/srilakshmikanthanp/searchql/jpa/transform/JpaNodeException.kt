package com.srilakshmikanthanp.searchql.jpa.transform

open class JpaNodeException(
  message: String = "Invalid JPA node",
  cause: Throwable? = null
): RuntimeException(message, cause)

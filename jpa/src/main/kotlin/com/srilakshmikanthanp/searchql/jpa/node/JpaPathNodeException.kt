package com.srilakshmikanthanp.searchql.jpa.node

open class JpaPathNodeException(
  message: String = "Invalid path node",
  cause: Throwable? = null
): JpaNodeException(message, cause)

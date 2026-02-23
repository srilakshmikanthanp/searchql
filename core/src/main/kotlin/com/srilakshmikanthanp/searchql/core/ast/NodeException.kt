package com.srilakshmikanthanp.searchql.core.ast

open class NodeException(
  message: String = "Invalid JPA node",
  cause: Throwable? = null
): RuntimeException(message, cause)

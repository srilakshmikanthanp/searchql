package com.srilakshmikanthanp.searchql.core.ast

open class ExpressionNodeException(
  message: String = "Invalid expression node",
  cause: Throwable? = null
): NodeException(message, cause)

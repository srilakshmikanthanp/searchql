package com.srilakshmikanthanp.searchql.core.ast

open class ExpressionNodeTypeMismatchException(
  val expectedTypes: Array<Class<*>>,
  val actualType: Class<*>,
  message: String = "Expected expression types ${expectedTypes.joinToString(", ")}, but got $actualType",
  cause: Throwable? = null
): ExpressionNodeException(message, cause)

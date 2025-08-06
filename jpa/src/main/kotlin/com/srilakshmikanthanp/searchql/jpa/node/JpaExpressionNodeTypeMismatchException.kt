package com.srilakshmikanthanp.searchql.jpa.node

open class JpaExpressionNodeTypeMismatchException(
  val expectedTypes: Array<Class<*>>,
  val actualType: Class<*>,
  message: String = "Expected expression types ${expectedTypes.joinToString(", ")}, but got $actualType",
  cause: Throwable? = null
): JpaExpressionNodeException(message, cause)

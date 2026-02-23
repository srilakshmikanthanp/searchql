package com.srilakshmikanthanp.searchql.jpa.transform

open class JpaPathNodeTypeMismatchException(
  val expectedTypes: Array<Class<*>>,
  val actualType: Class<*>,
  message: String = "Expected expression type ${expectedTypes.joinToString(", ")}, but got $actualType",
  cause: Throwable? = null
): JpaPathNodeException(message, cause)

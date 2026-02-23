package com.srilakshmikanthanp.searchql.jpa.transform

open class JpaNodeMismatchException(
  val expectedTypes: Array<Class<*>>,
  val actualType: Class<*>,
  message: String = "Expected node types ${expectedTypes.joinToString(", ")}, but got $actualType",
  cause: Throwable? = null
): JpaNodeException(message, cause)

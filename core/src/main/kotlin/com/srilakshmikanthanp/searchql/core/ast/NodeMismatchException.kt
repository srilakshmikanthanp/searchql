package com.srilakshmikanthanp.searchql.core.ast

open class NodeMismatchException(
  val expectedTypes: Array<Class<*>>,
  val actualType: Class<*>,
  message: String = "Expected node types ${expectedTypes.joinToString(", ")}, but got $actualType",
  cause: Throwable? = null
): NodeException(message, cause)

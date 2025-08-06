package com.srilakshmikanthanp.searchql.jpa.callable

class JpaCallableArgumentCountMismatchException(
  val callableName: String,
  val expectedCount: Int,
  val actualCount: Int
) : Exception(
  "Callable with name '$callableName' expected $expectedCount arguments but received $actualCount."
)

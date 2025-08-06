package com.srilakshmikanthanp.searchql.jpa.callable

class JpaCallableNotFoundException(
  val callableName: String
) : Exception(
  "Callable with name '$callableName' not found. "
)

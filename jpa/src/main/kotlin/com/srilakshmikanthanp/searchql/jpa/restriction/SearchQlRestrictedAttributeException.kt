package com.srilakshmikanthanp.searchql.jpa.restriction

class SearchQlRestrictedAttributeException(
  message: String = "Restricted attribute",
  cause: Throwable? = null
) : Exception(message, cause)

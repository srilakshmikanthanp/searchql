package com.srilakshmikanthanp.searchql.jpa.restriction

class SearchQLRestrictedAttributeException(
  message: String = "Restricted attribute",
  cause: Throwable? = null
) : Exception(message, cause)

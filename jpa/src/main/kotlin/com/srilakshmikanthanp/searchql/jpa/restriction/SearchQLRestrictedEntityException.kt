package com.srilakshmikanthanp.searchql.jpa.restriction

class SearchQLRestrictedEntityException(
  message: String = "Restricted attribute",
  cause: Throwable? = null
) : Exception(message, cause)

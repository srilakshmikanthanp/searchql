package com.srilakshmikanthanp.searchql.jpa.restriction

class SearchQlRestrictedEntityException(
  message: String = "Restricted attribute",
  cause: Throwable? = null
) : Exception(message, cause)

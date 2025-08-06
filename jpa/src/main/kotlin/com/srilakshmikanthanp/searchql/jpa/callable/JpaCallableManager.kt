package com.srilakshmikanthanp.searchql.jpa.callable

interface JpaCallableManager: JpaCallableProvider {
  fun registerCallable(name: String, callable: JpaCallable<*>)
  fun unregisterCallable(name: String)
  fun hasCallable(name: String): Boolean
  fun clearCallables()
}

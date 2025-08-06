package com.srilakshmikanthanp.searchql.jpa.callable

import java.util.Optional

class MapJpaCallableManager: JpaCallableManager {
  private val callables: MutableMap<String, JpaCallable<*>> = mutableMapOf()

  override fun registerCallable(name: String, callable: JpaCallable<*>) {
    if (callables.putIfAbsent(name, callable) != null) {
      throw IllegalArgumentException("Callable with name '$name' already exists.")
    }
  }

  override fun unregisterCallable(name: String) {
    callables.remove(name) ?: throw IllegalArgumentException("Callable with name '$name' does not exist.")
  }

  override fun hasCallable(name: String): Boolean {
    return callables.containsKey(name)
  }

  override fun clearCallables() {
    callables.clear()
  }

  override fun getCallable(name: String): Optional<JpaCallable<*>> {
    return Optional.ofNullable(callables[name])
  }
}

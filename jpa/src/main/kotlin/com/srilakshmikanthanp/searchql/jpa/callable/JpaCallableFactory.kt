package com.srilakshmikanthanp.searchql.jpa.callable

object JpaCallableFactory {
  fun createCallableManager(): JpaCallableManager {
    return MapJpaCallableManager()
  }
}

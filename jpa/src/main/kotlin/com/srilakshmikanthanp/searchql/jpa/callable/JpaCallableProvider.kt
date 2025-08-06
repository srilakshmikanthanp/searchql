package com.srilakshmikanthanp.searchql.jpa.callable

import java.util.Optional

interface JpaCallableProvider {
  fun getCallable(name: String): Optional<JpaCallable<*>>
}

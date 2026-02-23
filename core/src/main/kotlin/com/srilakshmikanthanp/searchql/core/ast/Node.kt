package com.srilakshmikanthanp.searchql.core.ast

sealed interface Node {
  fun <T> accept(visitor: NodeVisitor<T>): T
}

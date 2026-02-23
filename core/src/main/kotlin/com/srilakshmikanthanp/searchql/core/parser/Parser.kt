package com.srilakshmikanthanp.searchql.core.parser

import com.srilakshmikanthanp.searchql.core.ast.Node

interface Parser {
  fun parse(input: String): Node
}

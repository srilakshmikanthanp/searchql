package com.srilakshmikanthanp.searchql.core.parser

import com.srilakshmikanthanp.searchql.core.ast.AstBuilderVisitor
import com.srilakshmikanthanp.searchql.core.ast.Node
import com.srilakshmikanthanp.searchql.core.grammar.QueryLexer
import com.srilakshmikanthanp.searchql.core.grammar.QueryParser
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream

class AntlrParser : Parser {
  override fun parse(input: String): Node {
    val inputStream = CharStreams.fromString(input)
    val lexer = QueryLexer(inputStream)
    val tokenStream = CommonTokenStream(lexer)
    val parser = QueryParser(tokenStream)
    val tree = parser.expression()
    val astBuilder = AstBuilderVisitor()
    return tree.accept(astBuilder)
  }
}

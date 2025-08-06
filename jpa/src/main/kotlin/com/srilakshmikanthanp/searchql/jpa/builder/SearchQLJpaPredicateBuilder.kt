package com.srilakshmikanthanp.searchql.jpa.builder

import com.srilakshmikanthanp.searchql.core.grammar.QueryLexer
import com.srilakshmikanthanp.searchql.core.grammar.QueryParser
import com.srilakshmikanthanp.searchql.jpa.callable.JpaCallableProvider
import com.srilakshmikanthanp.searchql.jpa.node.asPredicateNode
import com.srilakshmikanthanp.searchql.jpa.visitor.JpaQueryTransformVisitor
import jakarta.persistence.EntityManager
import jakarta.persistence.criteria.CriteriaBuilder
import jakarta.persistence.criteria.Predicate
import jakarta.persistence.criteria.Root
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream

class SearchQLJpaPredicateBuilder private constructor(query: String) {
  private val tree: QueryParser.ExpressionContext

  init {
    val inputStream = CharStreams.fromString(query)
    val lexer = QueryLexer(inputStream)
    val tokenStream = CommonTokenStream(lexer)
    val parser = QueryParser(tokenStream)
    this.tree = parser.expression()
  }

  fun <T> toPredicate(
    jpaCallableProvider: JpaCallableProvider,
    entityManager: EntityManager,
    root: Root<T>,
    criteriaBuilder: CriteriaBuilder,
  ): Predicate {
    return tree.accept(JpaQueryTransformVisitor(jpaCallableProvider, entityManager, root, criteriaBuilder)).asPredicateNode().predicate
  }

  companion object {
    fun parse(query: String): SearchQLJpaPredicateBuilder {
      return SearchQLJpaPredicateBuilder(query)
    }
  }
}

package com.srilakshmikanthanp.searchql.jpa.builder

import com.srilakshmikanthanp.searchql.core.grammar.QueryLexer
import com.srilakshmikanthanp.searchql.core.grammar.QueryParser
import com.srilakshmikanthanp.searchql.jpa.callable.JpaCallableManager
import com.srilakshmikanthanp.searchql.jpa.callable.MapJpaCallableManager
import com.srilakshmikanthanp.searchql.jpa.node.asPredicateNode
import com.srilakshmikanthanp.searchql.jpa.visitor.JpaQueryTransformVisitor
import jakarta.persistence.EntityManager
import jakarta.persistence.criteria.CriteriaBuilder
import jakarta.persistence.criteria.Predicate
import jakarta.persistence.criteria.Root
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream

class JpaPredicateBuilder(callableManager: JpaCallableManager = MapJpaCallableManager()) {
  private val jpaCallableManager: JpaCallableManager = MapJpaCallableManager()

  inner class SearchQlQuery(val query: String) {
    private val tree: QueryParser.ExpressionContext

    init {
      val inputStream = CharStreams.fromString(query)
      val lexer = QueryLexer(inputStream)
      val tokenStream = CommonTokenStream(lexer)
      val parser = QueryParser(tokenStream)
      this.tree = parser.expression()
    }

    fun <T> toPredicate(
      entityManager: EntityManager,
      root: Root<T>,
      criteriaBuilder: CriteriaBuilder,
    ): Predicate {
      val visitor = JpaQueryTransformVisitor(jpaCallableManager, entityManager, root, criteriaBuilder)
      val node = tree.accept(visitor)
      return node.asPredicateNode().predicate
    }
  }

  init {
    for (name in callableManager.getCallableNames()) {
      this.jpaCallableManager.registerCallable(name, callableManager.getCallable(name).orElseThrow())
    }
  }

  fun parse(query: String): SearchQlQuery {
    return SearchQlQuery(query)
  }
}

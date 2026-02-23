package com.srilakshmikanthanp.searchql.jpa

import com.srilakshmikanthanp.searchql.core.ast.Node
import com.srilakshmikanthanp.searchql.core.parser.AntlrParser
import com.srilakshmikanthanp.searchql.jpa.callable.JpaCallableManager
import com.srilakshmikanthanp.searchql.jpa.callable.MapJpaCallableManager
import com.srilakshmikanthanp.searchql.jpa.transform.JpaQueryTransformVisitor
import com.srilakshmikanthanp.searchql.jpa.transform.asPredicateNode
import jakarta.persistence.EntityManager
import jakarta.persistence.criteria.CriteriaBuilder
import jakarta.persistence.criteria.Predicate
import jakarta.persistence.criteria.Root

class JpaPredicateBuilder(callableManager: JpaCallableManager = MapJpaCallableManager()) {
  private val jpaCallableManager: JpaCallableManager = MapJpaCallableManager()

  inner class SearchQlQuery(val query: String) {
    private val tree: Node = AntlrParser().parse(query)

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

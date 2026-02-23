package com.srilakshmikanthanp.searchql.jpa.callable.builtin

import com.srilakshmikanthanp.searchql.jpa.callable.JpaCallable
import com.srilakshmikanthanp.searchql.jpa.callable.JpaCallableArgument
import com.srilakshmikanthanp.searchql.jpa.callable.JpaCallableArgumentVariadic
import com.srilakshmikanthanp.searchql.jpa.transform.JpaExpressionNode
import com.srilakshmikanthanp.searchql.jpa.transform.asType
import jakarta.persistence.criteria.CriteriaBuilder

class ConcatCallable: JpaCallable<String> {
  override fun invoke(criteriaBuilder: CriteriaBuilder, vararg args: JpaExpressionNode<*>): JpaExpressionNode<String> {
    return JpaExpressionNode(criteriaBuilder.function("CONCAT", String::class.java, *args.map { it.asType<String>().expression }.toTypedArray()))
  }

  override fun getArgumentTypes(): List<JpaCallableArgument> {
    return listOf()
  }

  override fun getVariadicArgumentType(): JpaCallableArgumentVariadic {
    return JpaCallableArgumentVariadic(type = String::class.java)
  }
}

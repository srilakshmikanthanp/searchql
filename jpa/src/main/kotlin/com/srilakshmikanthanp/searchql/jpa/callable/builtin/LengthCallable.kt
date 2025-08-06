package com.srilakshmikanthanp.searchql.jpa.callable.builtin

import com.srilakshmikanthanp.searchql.jpa.callable.JpaCallable
import com.srilakshmikanthanp.searchql.jpa.callable.JpaCallableArgument
import com.srilakshmikanthanp.searchql.jpa.callable.JpaCallableArgumentVariadic
import com.srilakshmikanthanp.searchql.jpa.node.JpaExpressionNode
import com.srilakshmikanthanp.searchql.jpa.node.asType
import jakarta.persistence.criteria.CriteriaBuilder

class LengthCallable : JpaCallable<Int> {
  override fun invoke(criteriaBuilder: CriteriaBuilder, vararg args: JpaExpressionNode<*>): JpaExpressionNode<Int> {
    return JpaExpressionNode(criteriaBuilder.length(args[0].asType<String>().expression))
  }

  override fun getArgumentTypes(): List<JpaCallableArgument> {
    return listOf(JpaCallableArgument(String::class.java))
  }

  override fun getVariadicArgumentType(): JpaCallableArgumentVariadic? {
    return null
  }
}

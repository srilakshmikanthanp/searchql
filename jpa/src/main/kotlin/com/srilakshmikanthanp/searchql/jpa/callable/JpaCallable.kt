package com.srilakshmikanthanp.searchql.jpa.callable

import com.srilakshmikanthanp.searchql.jpa.node.JpaExpressionNode
import jakarta.persistence.criteria.CriteriaBuilder
import jakarta.persistence.criteria.Expression

interface JpaCallable<T> {
  /**
   * This method is used to invoke the callable with the provided arguments.
   * @param args - The validated arguments to pass to the callable
   */
  fun invoke(criteriaBuilder: CriteriaBuilder, vararg args: JpaExpressionNode<*>): JpaExpressionNode<T>

  /**
   * This method returns the types of the parameters that the callable expects.
   * @returns An array of CallableArgument instances representing the parameter types
   * may return empty array if no parameters are expected
   */
  fun getArgumentTypes(): List<JpaCallableArgument>

  /**
   * This method returns the type of the variadic parameter if it exists.
   * @returns A variadic instance representing the variadic parameter
   * type, or null if not applicable, it is considered last in the list of parameters
   * in addition to the regular parameters.
   */
  fun getVariadicArgumentType(): JpaCallableArgumentVariadic?
}

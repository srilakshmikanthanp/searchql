package com.srilakshmikanthanp.searchql.jpa.transform

import jakarta.persistence.criteria.CriteriaBuilder
import jakarta.persistence.criteria.Expression

open class JpaExpressionNumberNode<T : Number>(
  override val expression: Expression<T>
) : JpaExpressionNode<T>(expression),
  JpaAddableNode,
  JpaSubtractNode,
  JpaMultipliableNode,
  JpaDividableNode,
  JpaPowerNode,
  JpsUnaryMinusNode,
  JpsUnaryPlusNode,
  JpaGtNode,
  JpaGteNode,
  JpaLtNode,
  JpaLteNode {
  override fun add(
    cb: CriteriaBuilder,
    other: JpaNode
  ): JpaNode {
    cb.sum(
      this.expression,
      other.asExpressionNumberNode().expression
    ).let {
      return JpaExpressionNumberNode<Number>(it)
    }
  }

  override fun subtract(
    cb: CriteriaBuilder,
    other: JpaNode
  ): JpaNode {
    cb.diff(
      this.expression,
      other.asExpressionNumberNode().expression
    ).let {
      return JpaExpressionNumberNode(it)
    }
  }

  override fun multiply(
    cb: CriteriaBuilder,
    other: JpaNode
  ): JpaNode {
    cb.prod(
      this.expression,
      other.asExpressionNumberNode().expression
    ).let {
      return JpaExpressionNumberNode(it)
    }
  }

  override fun divide(
    cb: CriteriaBuilder,
    other: JpaNode
  ): JpaNode {
    cb.quot(
      this.expression,
      other.asExpressionNumberNode().expression
    ).let {
      return JpaExpressionNumberNode(it)
    }
  }

  override fun power(
    cb: CriteriaBuilder,
    other: JpaNode
  ): JpaNode {
    cb.power(
      this.expression,
      other.asExpressionNumberNode().expression
    ).let {
      return JpaExpressionNumberNode(it)
    }
  }

  override fun unaryMinus(
    cb: CriteriaBuilder
  ): JpaNode {
    cb.neg(
      this.expression
    ).let {
      return JpaExpressionNumberNode(it)
    }
  }

  override fun unaryPlus(
    cb: CriteriaBuilder
  ): JpaNode {
    return this
  }

  override fun gt(
    cb: CriteriaBuilder,
    other: JpaNode
  ): JpaNode {
    cb.gt(
      this.expression,
      other.asExpressionNumberNode().expression
    ).let {
      return JpaExpressionBooleanNode(it)
    }
  }

  override fun gte(
    cb: CriteriaBuilder,
    other: JpaNode
  ): JpaNode {
    cb.ge(
      this.expression,
      other.asExpressionNumberNode().expression
    ).let {
      return JpaExpressionBooleanNode(it)
    }
  }

  override fun lt(
    cb: CriteriaBuilder,
    other: JpaNode
  ): JpaNode {
    cb.lt(
      this.expression,
      other.asExpressionNumberNode().expression
    ).let {
      return JpaExpressionBooleanNode(it)
    }
  }

  override fun lte(
    cb: CriteriaBuilder,
    other: JpaNode
  ): JpaNode {
    cb.le(
      this.expression,
      other.asExpressionNumberNode().expression
    ).let {
      return JpaExpressionBooleanNode(it)
    }
  }
}

fun JpaNode.asExpressionNumberNode(): JpaExpressionNumberNode<out Number> {
  if (this !is JpaExpressionNumberNode<*>) {
    throw JpaNodeMismatchException(
      expectedTypes = arrayOf(JpaExpressionNode::class.java),
      actualType = this::class.java
    )
  }
  return this
}

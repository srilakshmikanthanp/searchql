package com.srilakshmikanthanp.searchql.jpa.visitor

import com.srilakshmikanthanp.searchql.core.grammar.QueryParser
import com.srilakshmikanthanp.searchql.core.grammar.QueryParserBaseVisitor
import com.srilakshmikanthanp.searchql.jpa.callable.JpaCallableArgumentCountMismatchException
import com.srilakshmikanthanp.searchql.jpa.callable.JpaCallableNotFoundException
import com.srilakshmikanthanp.searchql.jpa.callable.JpaCallableProvider
import com.srilakshmikanthanp.searchql.jpa.extensions.hasProperty
import com.srilakshmikanthanp.searchql.jpa.extensions.isAssociation
import com.srilakshmikanthanp.searchql.jpa.extensions.isRestricted
import com.srilakshmikanthanp.searchql.jpa.extensions.isRestrictedAttribute
import com.srilakshmikanthanp.searchql.jpa.node.*
import com.srilakshmikanthanp.searchql.jpa.restriction.SearchQlRestrictedAttributeException
import com.srilakshmikanthanp.searchql.jpa.restriction.SearchQlRestrictedEntityException
import jakarta.persistence.EntityManager
import jakarta.persistence.criteria.CriteriaBuilder
import jakarta.persistence.criteria.From
import jakarta.persistence.criteria.Root

class JpaQueryTransformVisitor<T>(
  private val jpaCallableProvider: JpaCallableProvider,
  private val entityManager: EntityManager,
  private val root: Root<T>,
  private val criteriaBuilder: CriteriaBuilder,
): QueryParserBaseVisitor<JpaNode>() {
  override fun visitMultiplication(ctx: QueryParser.MultiplicationContext): JpaNode {
    return ctx.left.accept(this).asMultipliableNode().multiply(criteriaBuilder, ctx.right.accept(this))
  }

  override fun visitOr(ctx: QueryParser.OrContext): JpaNode {
    return ctx.left.accept(this).asOrNode().or(criteriaBuilder, ctx.right.accept(this))
  }

  override fun visitNumericLiteral(ctx: QueryParser.NumericLiteralContext): JpaNode {
    return JpaExpressionDoubleNode(criteriaBuilder.literal(ctx.text.toDouble()))
  }

  override fun visitLessThanOrEqual(ctx: QueryParser.LessThanOrEqualContext): JpaNode {
    return ctx.left.accept(this).asLteNode().lte(criteriaBuilder, ctx.right.accept(this))
  }

  override fun visitIn(ctx: QueryParser.InContext): JpaNode {
    val left = ctx.item.accept(this).asExpressionNode()
    val right = ctx.elements.map { it.accept(this).asExpressionNode() }
    val result = left.expression.`in`(right.map { it.expression })
    return JpaPredicateNode(result)
  }

  override fun visitUnaryPlus(ctx: QueryParser.UnaryPlusContext): JpaNode {
    return visit(ctx.expression())
  }

  override fun visitNotIn(ctx: QueryParser.NotInContext): JpaNode {
    val left = ctx.item.accept(this).asExpressionNode()
    val right = ctx.elements.map { it.accept(this).asExpressionNode() }
    val result = criteriaBuilder.not(left.expression.`in`(right.map { it.expression }))
    return JpaPredicateNode(result)
  }

  override fun visitExponentiation(ctx: QueryParser.ExponentiationContext): JpaNode {
    return ctx.left.accept(this).asPowerNode().power(criteriaBuilder, ctx.right.accept(this))
  }

  override fun visitGreaterThanOrEqual(ctx: QueryParser.GreaterThanOrEqualContext): JpaNode {
    return ctx.left.accept(this).asGteNode().gte(criteriaBuilder, ctx.right.accept(this))
  }

  override fun visitFalseLiteral(ctx: QueryParser.FalseLiteralContext): JpaNode {
    return JpaExpressionBooleanNode(criteriaBuilder.literal(false))
  }

  override fun visitSingleQuotedStringLiteral(ctx: QueryParser.SingleQuotedStringLiteralContext): JpaNode {
    return JpaExpressionStringNode(criteriaBuilder.literal(ctx.text.removeSurrounding(SINGLE_QUOTE, SINGLE_QUOTE)))
  }

  override fun visitLessThan(ctx: QueryParser.LessThanContext): JpaNode {
    return ctx.left.accept(this).asLtNode().lt(criteriaBuilder, ctx.right.accept(this))
  }

  override fun visitBinaryIntegerLiteral(ctx: QueryParser.BinaryIntegerLiteralContext): JpaNode {
    return JpaExpressionIntegerNode(criteriaBuilder.literal(ctx.text.toInt(2)))
  }

  override fun visitTrueLiteral(ctx: QueryParser.TrueLiteralContext): JpaNode {
    return JpaExpressionBooleanNode(criteriaBuilder.literal(true))
  }

  override fun visitGreaterThan(ctx: QueryParser.GreaterThanContext): JpaNode {
    return ctx.left.accept(this).asGtNode().gt(criteriaBuilder, ctx.right.accept(this))
  }

  override fun visitDivision(ctx: QueryParser.DivisionContext): JpaNode {
    return ctx.left.accept(this).asDividableNode().divide(criteriaBuilder, ctx.right.accept(this))
  }

  override fun visitSubExpression(ctx: QueryParser.SubExpressionContext): JpaNode {
    return ctx.expression().accept(this)
  }

  override fun visitOctalIntegerLiteral(ctx: QueryParser.OctalIntegerLiteralContext): JpaNode {
    return JpaExpressionIntegerNode(criteriaBuilder.literal(ctx.text.toInt(8)))
  }

  override fun visitAddition(ctx: QueryParser.AdditionContext): JpaNode {
    return ctx.left.accept(this).asAddableNode().add(criteriaBuilder, ctx.right.accept(this))
  }

  override fun visitModulo(ctx: QueryParser.ModuloContext): JpaNode {
    return ctx.left.accept(this).asModuloNode().modulo(criteriaBuilder, ctx.right.accept(this))
  }

  override fun visitUnaryMinus(ctx: QueryParser.UnaryMinusContext): JpaNode {
    return ctx.expression().accept(this).asUnaryMinusNode().unaryMinus(criteriaBuilder)
  }

  override fun visitNotEquals(ctx: QueryParser.NotEqualsContext): JpaNode {
    return ctx.left.accept(this).asNotEqNode().notEq(criteriaBuilder, ctx.right.accept(this))
  }

  override fun visitHexIntegerLiteral(ctx: QueryParser.HexIntegerLiteralContext): JpaNode {
    return JpaExpressionIntegerNode(criteriaBuilder.literal(ctx.text.toInt(16)))
  }

  override fun visitNot(ctx: QueryParser.NotContext): JpaNode {
    return ctx.expression().accept(this).asNotNode().not(criteriaBuilder)
  }

  override fun visitEquals(ctx: QueryParser.EqualsContext): JpaNode {
    val left = ctx.left.accept(this).asExpressionNode()
    val right = ctx.right.accept(this).asExpressionNode()
    val result = criteriaBuilder.equal(left.expression, right.expression)
    return JpaPredicateNode(result)
  }

  override fun visitSubtraction(ctx: QueryParser.SubtractionContext): JpaNode {
    return ctx.left.accept(this).asSubtractableNode().subtract(criteriaBuilder, ctx.right.accept(this))
  }

  override fun visitAnd(ctx: QueryParser.AndContext): JpaNode {
    return ctx.left.accept(this).asAndNode().and(criteriaBuilder, ctx.right.accept(this))
  }

  override fun visitDoubleQuotedStringLiteral(ctx: QueryParser.DoubleQuotedStringLiteralContext): JpaNode {
    return JpaExpressionStringNode(criteriaBuilder.literal(ctx.text.removeSurrounding(DOUBLE_QUOTE, DOUBLE_QUOTE)))
  }

  override fun visitNullLiteral(ctx: QueryParser.NullLiteralContext?): JpaNode {
    return JpaExpressionNode(criteriaBuilder.nullLiteral(Any::class.java))
  }

  override fun visitIdentifier(ctx: QueryParser.IdentifierContext): JpaNode {
    val metamodel = entityManager.metamodel.managedType(root.javaType)
    val property = ctx.text

    if (metamodel.isRestricted()) {
      throw SearchQlRestrictedEntityException("Access to the entity type '${metamodel.javaType.name}' is restricted")
    }

    if (!metamodel.hasProperty(property)) {
      throw JpaPathNodeException("Unknown property: $property in ${metamodel.javaType.name}")
    }

    if (metamodel.isRestrictedAttribute(property)) {
      throw SearchQlRestrictedAttributeException("Access to the field '$property' is restricted in ${metamodel.javaType.name}")
    }

    return if (metamodel.isAssociation(property)) {
      JpaPathNode(root.join<Any, Any>(property))
    } else {
      JpaPathNode(root.get<Any>(property))
    }
  }

  override fun visitObjectAccess(ctx: QueryParser.ObjectAccessContext): JpaNode? {
    val base = ctx.base.accept(this).asExpressionNode().asPathNode()
    val property = ctx.member.text

    val metamodel = try {
      entityManager.metamodel.managedType(base.path.javaType)
    } catch (e: IllegalArgumentException) {
      throw JpaPathNodeException("Unknown type: ${root.javaType.name}", e)
    }

    if (metamodel.isRestricted()) {
      throw SearchQlRestrictedEntityException("Access to the entity type '${metamodel.javaType.name}' is restricted")
    }

    if (!metamodel.hasProperty(property)) {
      throw JpaPathNodeException("Unknown property: $property in ${metamodel.javaType.name}")
    }

    if (metamodel.isRestrictedAttribute(property)) {
      throw SearchQlRestrictedAttributeException("Access to the field '$property' is restricted in ${metamodel.javaType.name}")
    }

    val nextPath = if (metamodel.isAssociation(property)) {
      (base.path as From<*, *>).join<Any, Any>(property)
    } else {
      base.path.get(property)
    }

    return JpaPathNode(nextPath)
  }

  override fun visitFunctionCall(ctx: QueryParser.FunctionCallContext): JpaNode {
    val arguments = ctx.arguments.map { it.accept(this).asExpressionNode() }
    val callable = jpaCallableProvider.getCallable(ctx.identifier.text).orElseThrow {
      JpaCallableNotFoundException("Callable with name '${ctx.identifier.text}' not found")
    }
    val argTypes = callable.getArgumentTypes()
    val variadicType = callable.getVariadicArgumentType()

    if (variadicType == null && arguments.size != argTypes.size || variadicType != null && arguments.size < argTypes.size) {
      throw JpaCallableArgumentCountMismatchException(
        expectedCount = argTypes.size,
        actualCount = arguments.size,
        callableName = ctx.identifier.text
      )
    }

    for (i in argTypes.indices) {
      if (!argTypes[i].type.isAssignableFrom(arguments[i].expression.javaType)) {
        throw JpaExpressionNodeTypeMismatchException(
          expectedTypes = arrayOf(argTypes[i].type),
          actualType = arguments[i].expression.javaType
        )
      }
    }

    if (variadicType == null) {
      return callable.invoke(criteriaBuilder, *arguments.toTypedArray())
    }

    for (i in argTypes.size until arguments.size) {
      if (!variadicType.type.isAssignableFrom(arguments[i].expression.javaType)) {
        throw JpaExpressionNodeTypeMismatchException(
          expectedTypes = arrayOf(variadicType.type),
          actualType = arguments[i].expression.javaType
        )
      }
    }

    return callable.invoke(criteriaBuilder, *arguments.toTypedArray())
  }

  companion object {
    private const val DOUBLE_QUOTE = "\""
    private const val SINGLE_QUOTE = "'"
  }
}

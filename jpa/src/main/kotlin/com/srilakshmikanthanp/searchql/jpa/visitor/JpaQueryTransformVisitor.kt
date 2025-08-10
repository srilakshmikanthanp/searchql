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
    val left = ctx.left.accept(this).asExpressionNode().asType<Number>()
    val right = ctx.right.accept(this).asExpressionNode().asType<Number>()
    val result = criteriaBuilder.prod(left.expression, right.expression)
    return JpaExpressionNode(result)
  }

  override fun visitOr(ctx: QueryParser.OrContext): JpaNode {
    val left = ctx.left.accept(this).asExpressionNode().asType<Boolean>()
    val right = ctx.right.accept(this).asExpressionNode().asType<Boolean>()
    val result = criteriaBuilder.or(left.expression, right.expression)
    return JpaPredicateNode(result)
  }

  override fun visitNumericLiteral(ctx: QueryParser.NumericLiteralContext): JpaNode {
    return JpaExpressionNode(criteriaBuilder.literal(ctx.text.toDouble()))
  }

  override fun visitLessThanOrEqual(ctx: QueryParser.LessThanOrEqualContext): JpaNode {
    val left = ctx.left.accept(this).asExpressionNode().asType<Number>()
    val right = ctx.right.accept(this).asExpressionNode().asType<Number>()
    val result = criteriaBuilder.le(left.expression, right.expression)
    return JpaExpressionNode(result)
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
    val left = ctx.left.accept(this).asExpressionNode().asType<Number>()
    val right = ctx.right.accept(this).asExpressionNode().asType<Number>()
    val result = criteriaBuilder.power(left.expression, right.expression)
    return JpaExpressionNode(result)
  }

  override fun visitGreaterThanOrEqual(ctx: QueryParser.GreaterThanOrEqualContext): JpaNode {
    val left = ctx.left.accept(this).asExpressionNode().asType<Number>()
    val right = ctx.right.accept(this).asExpressionNode().asType<Number>()
    val result = criteriaBuilder.ge(left.expression, right.expression)
    return JpaPredicateNode(result)
  }

  override fun visitFalseLiteral(ctx: QueryParser.FalseLiteralContext): JpaNode {
    return JpaExpressionNode(criteriaBuilder.literal(false))
  }

  override fun visitSingleQuotedStringLiteral(ctx: QueryParser.SingleQuotedStringLiteralContext): JpaNode {
    return JpaExpressionNode(criteriaBuilder.literal(ctx.text.removeSurrounding(SINGLE_QUOTE, SINGLE_QUOTE)))
  }

  override fun visitLessThan(ctx: QueryParser.LessThanContext): JpaNode {
    val left = ctx.left.accept(this).asExpressionNode().asType<Number>()
    val right = ctx.right.accept(this).asExpressionNode().asType<Number>()
    val result = criteriaBuilder.lt(left.expression, right.expression)
    return JpaPredicateNode(result)
  }

  override fun visitBinaryIntegerLiteral(ctx: QueryParser.BinaryIntegerLiteralContext): JpaNode {
    return JpaExpressionNode(criteriaBuilder.literal(ctx.text.toInt(2)))
  }

  override fun visitTrueLiteral(ctx: QueryParser.TrueLiteralContext): JpaNode {
    return JpaExpressionNode(criteriaBuilder.literal(true))
  }

  override fun visitGreaterThan(ctx: QueryParser.GreaterThanContext): JpaNode {
    val left = ctx.left.accept(this).asExpressionNode().asType<Number>()
    val right = ctx.right.accept(this).asExpressionNode().asType<Number>()
    val result = criteriaBuilder.gt(left.expression, right.expression)
    return JpaPredicateNode(result)
  }

  override fun visitDivision(ctx: QueryParser.DivisionContext): JpaNode {
    val left = ctx.left.accept(this).asExpressionNode().asType<Number>()
    val right = ctx.right.accept(this).asExpressionNode().asType<Number>()
    val result = criteriaBuilder.quot(left.expression, right.expression)
    return JpaExpressionNode(result)
  }

  override fun visitSubExpression(ctx: QueryParser.SubExpressionContext): JpaNode {
    return ctx.expression().accept(this)
  }

  override fun visitOctalIntegerLiteral(ctx: QueryParser.OctalIntegerLiteralContext): JpaNode {
    return JpaExpressionNode(criteriaBuilder.literal(ctx.text.toInt(8)))
  }

  override fun visitAddition(ctx: QueryParser.AdditionContext): JpaNode {
    val left = ctx.left.accept(this).asExpressionNode()
    val right = ctx.right.accept(this).asExpressionNode()

    if (!left.isTypeOf(String::class.java, Number::class.java)) {
      throw JpaExpressionNodeTypeMismatchException(
        expectedTypes = arrayOf(String::class.java, Number::class.java),
        actualType = left.expression.javaType
      )
    }

    if (!right.isTypeOf(String::class.java, Number::class.java)) {
      throw JpaExpressionNodeTypeMismatchException(
        expectedTypes = arrayOf(String::class.java, Number::class.java),
        actualType = right.expression.javaType
      )
    }

    return if (left.isTypeOf(Number::class.java)) {
      JpaExpressionNode(criteriaBuilder.sum(left.asType<Number>().expression, right.asType<Number>().expression))
    } else {
      JpaExpressionNode(criteriaBuilder.concat(left.asType<String>().expression, right.asType<String>().expression))
    }
  }

  override fun visitModulo(ctx: QueryParser.ModuloContext): JpaNode {
    val left = ctx.left.accept(this).asExpressionNode().asType<Int>()
    val right = ctx.right.accept(this).asExpressionNode().asType<Int>()
    val result = criteriaBuilder.mod(left.expression, right.expression)
    return JpaExpressionNode(result)
  }

  override fun visitUnaryMinus(ctx: QueryParser.UnaryMinusContext): JpaNode {
    val expression = ctx.expression().accept(this).asExpressionNode().asType<Number>()
    val result = criteriaBuilder.neg(expression.expression)
    return JpaExpressionNode(result)
  }

  override fun visitNotEquals(ctx: QueryParser.NotEqualsContext): JpaNode {
    val left = ctx.left.accept(this).asExpressionNode()
    val right = ctx.right.accept(this).asExpressionNode()
    val result = criteriaBuilder.notEqual(left.expression, right.expression)
    return JpaPredicateNode(result)
  }

  override fun visitHexIntegerLiteral(ctx: QueryParser.HexIntegerLiteralContext): JpaNode {
    return JpaExpressionNode(criteriaBuilder.literal(ctx.text.toInt(16)))
  }

  override fun visitNot(ctx: QueryParser.NotContext): JpaNode {
    val element = ctx.expression().accept(this).asExpressionNode().asType<Boolean>()
    val result = criteriaBuilder.not(element.expression)
    return JpaPredicateNode(result)
  }

  override fun visitEquals(ctx: QueryParser.EqualsContext): JpaNode {
    val left = ctx.left.accept(this).asExpressionNode()
    val right = ctx.right.accept(this).asExpressionNode()
    val result = criteriaBuilder.equal(left.expression, right.expression)
    return JpaPredicateNode(result)
  }

  override fun visitSubtraction(ctx: QueryParser.SubtractionContext): JpaNode {
    val left = ctx.left.accept(this).asExpressionNode().asType<Number>()
    val right = ctx.right.accept(this).asExpressionNode().asType<Number>()
    val result = criteriaBuilder.diff(left.expression, right.expression)
    return JpaExpressionNode(result)
  }

  override fun visitAnd(ctx: QueryParser.AndContext): JpaNode {
    val left = ctx.left.accept(this).asExpressionNode().asType<Boolean>()
    val right = ctx.right.accept(this).asExpressionNode().asType<Boolean>()
    val result = criteriaBuilder.and(left.expression, right.expression)
    return JpaPredicateNode(result)
  }

  override fun visitDoubleQuotedStringLiteral(ctx: QueryParser.DoubleQuotedStringLiteralContext): JpaNode {
    return JpaExpressionNode(criteriaBuilder.literal(ctx.text.removeSurrounding(DOUBLE_QUOTE, DOUBLE_QUOTE)))
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

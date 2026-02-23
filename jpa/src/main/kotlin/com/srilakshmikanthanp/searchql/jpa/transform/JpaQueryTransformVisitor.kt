package com.srilakshmikanthanp.searchql.jpa.transform

import com.srilakshmikanthanp.searchql.core.ast.AdditionNode
import com.srilakshmikanthanp.searchql.core.ast.AndNode
import com.srilakshmikanthanp.searchql.core.ast.BooleanLiteralNode
import com.srilakshmikanthanp.searchql.core.ast.DivisionNode
import com.srilakshmikanthanp.searchql.core.ast.EqualsNode
import com.srilakshmikanthanp.searchql.core.ast.ExponentiationNode
import com.srilakshmikanthanp.searchql.core.ast.FunctionCallNode
import com.srilakshmikanthanp.searchql.core.ast.GreaterThanNode
import com.srilakshmikanthanp.searchql.core.ast.IdentifierNode
import com.srilakshmikanthanp.searchql.core.ast.InNode
import com.srilakshmikanthanp.searchql.core.ast.IntegerLiteralNode
import com.srilakshmikanthanp.searchql.core.ast.LessThanNode
import com.srilakshmikanthanp.searchql.core.ast.ModuloNode
import com.srilakshmikanthanp.searchql.core.ast.MultiplicationNode
import com.srilakshmikanthanp.searchql.core.ast.NodeVisitor
import com.srilakshmikanthanp.searchql.core.ast.NotEqualsNode
import com.srilakshmikanthanp.searchql.core.ast.NotInNode
import com.srilakshmikanthanp.searchql.core.ast.NotNode
import com.srilakshmikanthanp.searchql.core.ast.NullExpressionNode
import com.srilakshmikanthanp.searchql.core.ast.NumericLiteralNode
import com.srilakshmikanthanp.searchql.core.ast.ObjectAccessNode
import com.srilakshmikanthanp.searchql.core.ast.OrExpressionNode
import com.srilakshmikanthanp.searchql.core.ast.StringLiteralNode
import com.srilakshmikanthanp.searchql.core.ast.SubtractionNode
import com.srilakshmikanthanp.searchql.core.ast.UnaryMinusNode
import com.srilakshmikanthanp.searchql.core.ast.UnaryPlusNode
import com.srilakshmikanthanp.searchql.jpa.callable.JpaCallableArgumentCountMismatchException
import com.srilakshmikanthanp.searchql.jpa.callable.JpaCallableNotFoundException
import com.srilakshmikanthanp.searchql.jpa.callable.JpaCallableProvider
import com.srilakshmikanthanp.searchql.jpa.extensions.hasProperty
import com.srilakshmikanthanp.searchql.jpa.extensions.isAssociation
import com.srilakshmikanthanp.searchql.jpa.extensions.isRestricted
import com.srilakshmikanthanp.searchql.jpa.extensions.isRestrictedAttribute
import com.srilakshmikanthanp.searchql.jpa.restriction.SearchQlRestrictedAttributeException
import com.srilakshmikanthanp.searchql.jpa.restriction.SearchQlRestrictedEntityException
import jakarta.persistence.EntityManager
import jakarta.persistence.criteria.CriteriaBuilder
import jakarta.persistence.criteria.From
import jakarta.persistence.criteria.Root
import jakarta.persistence.metamodel.ManagedType

class JpaQueryTransformVisitor<T>(
  private val jpaCallableProvider: JpaCallableProvider,
  private val entityManager: EntityManager,
  private val root: Root<T>,
  private val criteriaBuilder: CriteriaBuilder,
): NodeVisitor<JpaNode> {
  override fun visitUnaryPlus(expression: UnaryPlusNode): JpaNode {
    return expression.operand.accept(this)
  }

  override fun visitMultiplication(node: MultiplicationNode): JpaNode {
    return node.left.accept(this).asMultipliableNode().multiply(criteriaBuilder, node.right.accept(this))
  }

  override fun visitStringLiteral(node: StringLiteralNode): JpaNode {
    return JpaExpressionStringNode(criteriaBuilder.literal(node.literal))
  }

  override fun visitBooleanLiteral(node: BooleanLiteralNode): JpaNode {
    return JpaExpressionBooleanNode(criteriaBuilder.literal(node.literal))
  }

  override fun visitOr(node: OrExpressionNode): JpaNode {
    return node.left.accept(this).asPredicateNode().or(criteriaBuilder, node.right.accept(this))
  }

  override fun visitLessThan(node: LessThanNode): JpaNode {
    return node.left.accept(this).asLtNode().lt(criteriaBuilder, node.right.accept(this))
  }

  override fun visitNull(node: NullExpressionNode): JpaNode {
    return JpaExpressionNode(criteriaBuilder.nullLiteral(Any::class.java))
  }

  override fun visitNumericLiteral(literal: NumericLiteralNode): JpaNode {
    return JpaExpressionNumberNode(criteriaBuilder.literal(literal.literal))
  }

  override fun visitGreaterThan(node: GreaterThanNode): JpaNode {
    return node.left.accept(this).asGtNode().gt(criteriaBuilder, node.right.accept(this))
  }

  override fun visitIntegerLiteral(node: IntegerLiteralNode): JpaNode {
    return JpaExpressionNumberNode(criteriaBuilder.literal(node.literal))
  }

  override fun visitExponentiation(node: ExponentiationNode): JpaNode {
    return node.left.accept(this).asPowerNode().power(criteriaBuilder, node.right.accept(this))
  }

  override fun visitIdentifier(expression: IdentifierNode): JpaNode {
    val metamodel = entityManager.metamodel.managedType(root.javaType)
    val property = expression.name

    this.validateProperty(metamodel, property)

    return if (metamodel.isAssociation(property)) {
      JpaPathNode(root.join<Any, Any>(property))
    } else {
      JpaPathNode(root.get<Any>(property))
    }
  }

  override fun visitDivision(node: DivisionNode): JpaNode {
    return node.left.accept(this).asDividableNode().divide(criteriaBuilder, node.right.accept(this))
  }

  override fun visitObjectAccess(node: ObjectAccessNode): JpaNode {
    val base = node.base.accept(this).asExpressionNode().asPathNode()
    val property = node.member

    val metamodel = try {
      entityManager.metamodel.managedType(base.path.javaType)
    } catch (e: IllegalArgumentException) {
      throw JpaPathNodeException("Unknown type: ${root.javaType.name}", e)
    }

    this.validateProperty(metamodel, property)

    val nextPath = if (metamodel.isAssociation(property)) {
      (base.path as From<*, *>).join<Any, Any>(property)
    } else {
      base.path.get(property)
    }

    return JpaPathNode(nextPath)
  }

  override fun visitAdditionNode(node: AdditionNode): JpaNode {
    return node.left.accept(this).asAddableNode().add(criteriaBuilder, node.right.accept(this))
  }

  override fun visitUnaryMinus(node: UnaryMinusNode): JpaNode {
    return node.operand.accept(this).asUnaryMinusNode().unaryMinus(criteriaBuilder)
  }

  override fun visitNot(node: NotNode): JpaNode {
    return node.operand.accept(this).asPredicateNode().not(criteriaBuilder)
  }

  override fun visitEquals(node: EqualsNode): JpaNode {
    return node.left.accept(this).asEqNode().eq(criteriaBuilder, node.right.accept(this))
  }

  override fun visitNotEquals(node: NotEqualsNode): JpaNode {
    return node.left.accept(this).asNotEqNode().notEq(criteriaBuilder, node.right.accept(this))
  }

  override fun visitSubtraction(node: SubtractionNode): JpaNode {
    return node.left.accept(this).asSubtractableNode().subtract(criteriaBuilder, node.right.accept(this))
  }

  override fun visitAnd(node: AndNode): JpaNode {
    return node.left.accept(this).asPredicateNode().and(criteriaBuilder, node.right.accept(this))
  }

  override fun visitModulo(node: ModuloNode): JpaNode {
    return node.left.accept(this).asModuloNode().modulo(criteriaBuilder, node.right.accept(this))
  }

  override fun visitIn(node: InNode): JpaNode {
    val left = node.member.accept(this).asExpressionNode()
    val right = node.elements.map { it.accept(this).asExpressionNode() }
    val result = left.expression.`in`(right.map { it.expression })
    return JpaPredicateNode(result)
  }

  override fun visitNotIn(node: NotInNode): JpaNode {
    val left = node.member.accept(this).asExpressionNode()
    val right = node.elements.map { it.accept(this).asExpressionNode() }
    val result = criteriaBuilder.not(left.expression.`in`(right.map { it.expression }))
    return JpaPredicateNode(result)
  }

  override fun visitFunctionCall(node: FunctionCallNode): JpaNode {
    val arguments = node.arguments.map { it.accept(this).asExpressionNode() }

    val callable = jpaCallableProvider.getCallable(node.identifier).orElseThrow {
      JpaCallableNotFoundException("Callable with name '${node.identifier}' not found")
    }

    val argTypes = callable.getArgumentTypes()
    val variadicType = callable.getVariadicArgumentType()

    if (variadicType == null && arguments.size != argTypes.size || variadicType != null && arguments.size < argTypes.size) {
      throw JpaCallableArgumentCountMismatchException(
        expectedCount = argTypes.size,
        actualCount = arguments.size,
        callableName = node.identifier
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

  private fun validateProperty(metamodel: ManagedType<*>, property: String) {
    if (metamodel.isRestricted()) throw SearchQlRestrictedEntityException("Access to the entity type '${metamodel.javaType.name}' is restricted")
    if (!metamodel.hasProperty(property)) throw JpaPathNodeException("Unknown property: $property in ${metamodel.javaType.name}")
    if (metamodel.isRestrictedAttribute(property)) throw SearchQlRestrictedAttributeException("Access to the field '$property' is restricted in ${metamodel.javaType.name}")
  }
}

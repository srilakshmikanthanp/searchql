package com.srilakshmikanthanp.searchql.core.ast

sealed class MembershipNode(
  val member: ExpressionNode,
  val elements: List<ExpressionNode>
) : ExpressionNode

package com.srilakshmikanthanp.searchql.core.ast

sealed class UnaryExpressionNode(open val operand: ExpressionNode) : Node

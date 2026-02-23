package com.srilakshmikanthanp.searchql.core.ast

sealed class LiteralExpressionNode(open val literal: Any) : ExpressionNode

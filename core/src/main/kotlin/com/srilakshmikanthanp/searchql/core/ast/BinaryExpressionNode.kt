package com.srilakshmikanthanp.searchql.core.ast

sealed  class BinaryExpressionNode(open val left: Node, open val right: Node) : ExpressionNode

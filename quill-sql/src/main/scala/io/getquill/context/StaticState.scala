package io.getquill.context

import scala.quoted._
import io.getquill.Planter
import io.getquill.ReturnAction
import io.getquill.ast.Ast

case class StaticState(query: Unparticular.Query, lifts: List[Expr[Planter[?, ?]]], returnAction: Option[ReturnAction])(queryAst: =>Ast):
  def ast: Ast = queryAst
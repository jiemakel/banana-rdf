package org.w3.banana.binder

import org.w3.banana._
import scala.util._

trait FromLiteral[Rdf <: RDF, +T] {
  def fromLiteral(literal: Rdf#Literal): Try[T]
}

object FromLiteral {

  implicit def LiteralFromLiteral[Rdf <: RDF] = new FromLiteral[Rdf, Rdf#Literal] {
    def fromLiteral(literal: Rdf#Literal): Try[Rdf#Literal] = Success(literal)
  }

  implicit def FromPlainLiteralFromLiteral[Rdf <: RDF, T](implicit ops: RDFOps[Rdf], from: FromPlainLiteral[Rdf, T]) = new FromLiteral[Rdf, T] {
    def fromLiteral(literal: Rdf#Literal): Try[T] = ops.foldLiteral(literal)(
      pl => from.fromPlainLiteral(pl),
      tl => Failure(FailedConversion(s"expected PlainLiteral, got TypedLiteral $tl")),
      ll => Failure(FailedConversion(s"expected PlainLiteral, got LangLiteral $ll")))
  }

  implicit def FromTypedLiteralFromLiteral[Rdf <: RDF, T](implicit ops: RDFOps[Rdf], from: FromTypedLiteral[Rdf, T]) = new FromLiteral[Rdf, T] {
    def fromLiteral(literal: Rdf#Literal): Try[T] = ops.foldLiteral(literal)(
      pl => Failure(FailedConversion(s"expected TypedLiteral, got PlainLiteral $pl")),
      tl => from.fromTypedLiteral(tl),
      ll => Failure(FailedConversion(s"expected TypedLiteral, got LangLiteral $ll")))
  }

  implicit def FromLangLiteralFromLiteral[Rdf <: RDF, T](implicit ops: RDFOps[Rdf], from: FromLangLiteral[Rdf, T]) = new FromLiteral[Rdf, T] {
    def fromLiteral(literal: Rdf#Literal): Try[T] = ops.foldLiteral(literal)(
      pl => Failure(FailedConversion(s"expected LangLiteral, got PlainLiteral $pl")),
      tl => Failure(FailedConversion(s"expected LangLiteral, got TypedLiteral $tl")),
      ll => from.fromLangLiteral(ll))
  }

}

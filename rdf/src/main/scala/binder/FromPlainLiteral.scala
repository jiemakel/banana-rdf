package org.w3.banana.binder

import org.w3.banana._
import scala.util._

trait FromPlainLiteral[Rdf <: RDF, +T] {
  def fromPlainLiteral(tl: Rdf#PlainLiteral): Try[T]
}

object FromPlainLiteral {

  implicit def PlainLiteralFromPlainLiteral[Rdf <: RDF] = new FromPlainLiteral[Rdf, Rdf#PlainLiteral] {
    def fromPlainLiteral(ll: Rdf#PlainLiteral): Try[Rdf#PlainLiteral] = Success(ll)
  }

  implicit def StringFromPlainLiteral[Rdf <: RDF](implicit ops: RDFOps[Rdf]) = new FromPlainLiteral[Rdf, String] {
    import ops._
    def fromPlainLiteral(pl: Rdf#PlainLiteral): Try[String] = {
      Success(pl.lexicalForm)
    }
  }

}

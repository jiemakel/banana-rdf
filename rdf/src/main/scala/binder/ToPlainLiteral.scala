package org.w3.banana.binder

import org.w3.banana._

trait ToPlainLiteral[Rdf <: RDF, -T] {
  def toPlainLiteral(t: T): Rdf#PlainLiteral
}

object ToPlainLiteral {

  implicit def PlainLiteralToPlainLiteral[Rdf <: RDF] = new ToPlainLiteral[Rdf, Rdf#PlainLiteral] {
    def toPlainLiteral(t: Rdf#PlainLiteral): Rdf#PlainLiteral = t
  }

  implicit def StringToPlainLiteral[Rdf <: RDF](implicit ops: RDFOps[Rdf]) =
    new ToPlainLiteral[Rdf, String] {
      import ops._
      def toPlainLiteral(s: String): Rdf#PlainLiteral = PlainLiteral(s)
    }

}

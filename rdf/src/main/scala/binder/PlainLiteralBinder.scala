package org.w3.banana.binder

import org.w3.banana._
import scala.util._

trait PlainLiteralBinder[Rdf <: RDF, T] extends FromPlainLiteral[Rdf, T] with ToPlainLiteral[Rdf, T]

object PlainLiteralBinder {

  implicit def FromPlainLiteralToPlainLiteral2PlainLiteralBinder[Rdf <: RDF, T](implicit from: FromPlainLiteral[Rdf, T], to: ToPlainLiteral[Rdf, T]): PlainLiteralBinder[Rdf, T] =
    new PlainLiteralBinder[Rdf, T] {
      def fromPlainLiteral(ll: Rdf#PlainLiteral): Try[T] = from.fromPlainLiteral(ll)
      def toPlainLiteral(t: T): Rdf#PlainLiteral = to.toPlainLiteral(t)
    }

}

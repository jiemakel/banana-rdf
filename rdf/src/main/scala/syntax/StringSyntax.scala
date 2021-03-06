package org.w3.banana.syntax

import org.w3.banana._

trait StringSyntax[Rdf <: RDF] { self: Syntax[Rdf] =>

  implicit def stringW(s: String) = new StringW[Rdf](s)

}

class StringW[Rdf <: RDF](val s: String) extends AnyVal {

  def lang(langString: String)(implicit ops: RDFOps[Rdf]): Rdf#LangLiteral = ops.makeLangLiteral(s, ops.makeLang(langString))

}

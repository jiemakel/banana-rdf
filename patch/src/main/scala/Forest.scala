package org.w3.banana

/** in this context, Tree is a rooted directed tree, and we don't care care about the tree structure */
case class Tree[Rdf <: RDF](root: VarOrTerm[Rdf], nodes: Set[VarOrTerm[Rdf]])

object Forest {

  def empty[Rdf <: RDF]: Forest[Rdf] = Forest[Rdf](Set.empty[Tree[Rdf]], Map.empty[VarOrTerm[Rdf], Tree[Rdf]])

  /** builds a Forest out of a bunch of TriplePath-s */
  def apply[Rdf <: RDF](triples: Iterable[TriplePath[Rdf]]): Forest[Rdf] =
    triples.foldLeft(Forest.empty[Rdf])(_ add _)

}

/** a Forest is a disjoint set of Trees (i.e. they don't share any
  * node/root). It maintains a tree index, mapping a VarOrTerm to the
  * tree it belongs to */
case class Forest[Rdf <: RDF](trees: Set[Tree[Rdf]], index: Map[VarOrTerm[Rdf], Tree[Rdf]]) {

  def isSingleTree: Boolean = trees.size == 1

  def add(triplePath: TriplePath[Rdf]): Forest[Rdf] = {
    val TriplePath(s, _, o) = triplePath
    if (s == o) throw new AssertionError(s"subject and object cannot be the same for TriplePath $triplePath")
    (index.get(s), index.get(o)) match {
      // neither s nor o appear in any tree
      case (None, None) =>
        val tree = Tree(s, Set(s, o))
        Forest(trees + tree, index + (s -> tree) + (o -> tree))
      // s appears as a node in an existing tree
      case (Some(t), None) =>
        val tree = Tree(t.root, t.nodes + o)
        Forest(trees - t + tree, index ++ tree.nodes.map(_ -> tree))
      // o appears as a node in an existing tree, in the subject position
      case (None, Some(t@Tree(`o`, _))) =>
        val tree = Tree(s, t.nodes + s)
        Forest(trees - t + tree, index ++ tree.nodes.map(_ -> tree))
      // o appears as a node in an existing tree, NOT in the subject position
      case (None, Some(t)) =>
        throw new AssertionError(s"$s is another root for tree $t")
      // the TriplePath connects two existing trees
      case (Some(t1), Some(t2)) if t1 == t2  =>
        throw new AssertionError(s"$triplePath is binding that graph onto itself $t1")
      case (Some(t1), Some(t2)) =>
        val tree = Tree(t1.root, t1.nodes ++ t2.nodes)
        Forest(trees - t1 - t2 + tree, index ++ tree.nodes.map(_ -> tree))
    }
  }


}

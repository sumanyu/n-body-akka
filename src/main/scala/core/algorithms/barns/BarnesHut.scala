package core.algorithms.barns

//class BarnesHut extends NBodyAlgorithm {
//  private val q: Quad = Quad(0, 0, 2 * UniverseConstants.UNIVERSE_RADIUS)
//
//  def updateBodies(bodies: IndexedSeq[Body]): IndexedSeq[Body] = {
//    val tree: BHTree = new BHTree(q)
//    val bodiesInQuad = bodies.filter(q.contains)
//
//    bodiesInQuad.foreach { body =>
//      tree.insert(body)
//    }
//
//    bodiesInQuad.foreach { body =>
//      body.resetForce
//      tree.updateForce(body)
//      body.updateStateVariables
//    }
//  }
//}

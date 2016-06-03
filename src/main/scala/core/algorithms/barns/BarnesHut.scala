package core.algorithms.barns

//import core.algorithms.NBodyAlgorithm
//import core.models.Body
//import core.universe.UniverseConstants
//
//class BarnesHut extends NBodyAlgorithm {
//  private val universeQuad = Quad(0, 0, 2 * UniverseConstants.UNIVERSE_RADIUS)
//
//  def updateBodies(bodies: IndexedSeq[Body]): IndexedSeq[Body] = {
//    val tree = new BHTree(universeQuad)
//    val bodiesInQuad = bodies.filter(universeQuad.contains)
//
//    bodiesInQuad.foreach(tree.insert)
//    bodiesInQuad.map(_.resetForce).map(tree.updateForce).map(_.updateStateVariables())
//  }
//}

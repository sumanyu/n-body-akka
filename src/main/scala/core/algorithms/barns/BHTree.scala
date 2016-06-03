package core.algorithms.barns

import core.models.Body

private[barns] class BHTree(q: Quad) {
  private val THETA: Double = 0.5

  private var bodyOpt: Option[Body] = None
  private var northWestTree: Option[BHTree] = None
  private var northEastTree: Option[BHTree] = None
  private var southWestTree: Option[BHTree] = None
  private var southEastTree: Option[BHTree] = None

  implicit def bhTreeToOpt(bHTree: BHTree): Option[BHTree] = Some(bHTree)
  implicit def bhTreeOptToBHtree(bHTreeOpt: Option[BHTree]): BHTree = bHTreeOpt.get

  implicit def bodyToOpt(body: Body): Option[Body] = Some(body)
  implicit def bodyOptToBody(bodyOpt: Option[Body]): Body = bodyOpt.get

  def insert(b: Body) {
    if (bodyOpt.isEmpty)
      bodyOpt = b
    else {
      if (currentTreeIsExternal) {

        northWestTree = new BHTree(q.northWest)
        northEastTree = new BHTree(q.northEast)
        southEastTree = new BHTree(q.southEast)
        southWestTree = new BHTree(q.southWest)

        putBody(bodyOpt)
      }

      bodyOpt += b
      putBody(b)
    }
  }

  private def putBody(b: Body) {
    if (q.northWest.contains(b)) northWestTree.insert(b)
    else if (q.northEast.contains(b)) northEastTree.insert(b)
    else if (q.southEast.contains(b)) southEastTree.insert(b)
    else southWestTree.insert(b)
  }

  private def currentTreeIsExternal: Boolean = {
    IndexedSeq(northWestTree, northEastTree, southWestTree, southEastTree).forall(_.isEmpty)
  }

  def updateForce(b: Body): Body = {
    bodyOpt match {
      case Some(body) if body != b =>
        if (currentTreeIsExternal)
          b.addForceFrom(body)
        else {
          val s: Double = q.sideLength
          val d: Double = body.distance(b)
          if ((s / d) < THETA)
            b.addForceFrom(body)
          else {
            northWestTree.updateForce(b)
            northEastTree.updateForce(b)
            southWestTree.updateForce(b)
            southEastTree.updateForce(b)
          }
        }
      case _ => b
    }
  }
}
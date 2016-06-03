package core.algorithms.barns

import core.models.Body

private[barns] class BHTree(q: Quad) {
  private val THETA: Double = 0.5
  private var body: Body = null
  private var NW: BHTree = null
  private var NE: BHTree = null
  private var SW: BHTree = null
  private var SE: BHTree = null

  /**
    * Adds the given body to the tree.
    *
    * @param b
    * the body to add.
    */
  def insert(b: Body) {

    if (body == null) {
      body = b
      return
    }

    if (!isExternal) {
      body = body + b
      putBody(b)
    } else {
      NW = new BHTree(q.northWest)
      NE = new BHTree(q.northEast)
      SE = new BHTree(q.southEast)
      SW = new BHTree(q.southWest)
      putBody(body)
      putBody(b)
      body = body + b
    }
  }

  /**
    * Inserts a body into the correct sub quadrant by checking which one
    * contains it.
    *
    * @param b
    * the body to be inserted.
    */
  private def putBody(b: Body) {
    if (q.northWest.contains(b)) NW.insert(b)
    else if (q.northEast.contains(b)) NE.insert(b)
    else if (q.southEast.contains(b)) SE.insert(b)
    else SW.insert(b)
  }

  /**
    * Test if this node is an external node
    *
    * @return <code>true</code> if all sub nodes are <code>null</code>
    *         otherwise <code>false</code>.
    */
  private def isExternal: Boolean = {
    NW == null && NE == null && SW == null && SE == null
  }

  /**
    * Update the forces acting on a given body.
    *
    * @param b
    * the body to update the forces of.
    */
  def updateForce(b: Body) {
    if (body == null || (b == body)) return

    if (isExternal)
      b.addForceFrom(body)
    else {
      val s: Double = q.sideLength
      val d: Double = body.distance(b)
      if ((s / d) < THETA)
        b.addForceFrom(body)
      else {
        NW.updateForce(b)
        NE.updateForce(b)
        SW.updateForce(b)
        SE.updateForce(b)
      }
    }
  }
}
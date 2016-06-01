package core.algorithms.naive

import core.algorithms.NBodyAlgorithm
import core.models.Body

class NaiveQuadraticMethod extends NBodyAlgorithm {
  def updateBodies(bodies: IndexedSeq[Body]): IndexedSeq[Body] = {
    moveBodies(calculateForces(bodies))
  }

  private def calculateForces(bodies: IndexedSeq[Body]): IndexedSeq[Body] = {
    var bodiesMap: Map[Int, Body] = bodies.zipWithIndex.map { case (body, idx) => idx -> body }.toMap

    bodies.indices.dropRight(1).foreach { i =>
      bodies.indices.tail.foreach { j =>
        val bi = bodiesMap(i)
        val bj = bodiesMap(j)

        bodiesMap += i -> bi.addForce(bj)
        bodiesMap += j -> bj.addForce(bi)

//        val distance = bi.distance(bj)
//        val magnitude = (UniverseConstants.GRAVITATION * bi.mass * bj.mass) / math.pow(distance, 2)
//        val direction = bj.position - bi.position
//        val deltaForce = direction * magnitude / distance
//
//        bodiesMap += i -> (bi.force + deltaForce)
//        bodiesMap += j -> (bj.force - deltaForce)
      }
    }

    bodiesMap.values.toIndexedSeq
  }

  private def moveBodies(bodies: IndexedSeq[Body]): IndexedSeq[Body] = {
    bodies.map(_.updateStateVariables.resetForce)
  }
}

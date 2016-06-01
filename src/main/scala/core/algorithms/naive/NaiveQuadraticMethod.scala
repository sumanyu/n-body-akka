package core.algorithms.naive

import core.algorithms.NBodyAlgorithm
import core.models.Body
import core.universe.UniverseConstants

class NaiveQuadraticMethod extends NBodyAlgorithm {
  def updateBodies(bodies: IndexedSeq[Body]): IndexedSeq[Body] = {
    moveBodies(calculateForces(bodies))
  }

  private def calculateForces(bodies: IndexedSeq[Body]): IndexedSeq[Body] = {
    bodies.dropRight(1).map { b1 =>
      bodies.tail.map { b2 =>
        val distance = b1.distance(b2)
        val magnitude = (UniverseConstants.GRAVITATION * b1.mass * b2.mass) / math.pow(distance, 2)
        val direction = b2.position - b1.position

      }
    }
  }

  private def moveBodies(bodies: IndexedSeq[Body]): IndexedSeq[Body] = {
    bodies.map(_.updateStateVariables.resetForce)
  }
}

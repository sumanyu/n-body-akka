package core.algorithms.naive

import core.algorithms.NBodyAlgorithm
import core.models.Body

class NaiveQuadraticMethod extends NBodyAlgorithm {
  def updateBodies(bodies: IndexedSeq[Body]): IndexedSeq[Body] = moveBodies(calculateForces(bodies))

  private def moveBodies(bodies: IndexedSeq[Body]): IndexedSeq[Body] = bodies.map(_.updateStateVariables().resetForce)

  private def calculateForces(bodies: IndexedSeq[Body]): IndexedSeq[Body] = {
    var bodiesMap: Map[Int, Body] = bodies.zipWithIndex.map { case (body, idx) => idx -> body }.toMap

    val length = bodies.length
    bodies.indices.dropRight(1).foreach { i =>
      (i+1 until length).foreach { j =>
        val bi = bodiesMap(i)
        val bj = bodiesMap(j)

        val forceOnBiByBj = bi.forceBetween(bj)
        val forceOnBjByBi = forceOnBiByBj * -1

        bodiesMap += i -> bi.applyForce(forceOnBiByBj)
        bodiesMap += j -> bj.applyForce(forceOnBjByBi)
      }
    }

    bodiesMap.values.toIndexedSeq
  }
}

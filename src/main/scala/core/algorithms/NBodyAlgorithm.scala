package core.algorithms

import core.models.Body

trait NBodyAlgorithm {
  def updateBodies(bodies: IndexedSeq[Body]): IndexedSeq[Body]
}

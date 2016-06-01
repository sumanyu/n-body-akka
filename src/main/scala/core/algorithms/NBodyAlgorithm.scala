package core.algorithms

import core.models.Body

//Plug in various implementations to update position / velocity / force for each body
trait NBodyAlgorithm {
  def updateBodies(bodies: IndexedSeq[Body]): IndexedSeq[Body]
}

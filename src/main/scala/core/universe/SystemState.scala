package core.universe

import java.util

import core.models.Body
import collection.JavaConverters._

//Container for bodies, share mutable state between GUI and main simulation
class SystemState {
  private var bodies = IndexedSeq[Body]()

  def getJavaBodies: util.List[Body] = bodies.asJava
  def setNewState(_bodies: IndexedSeq[Body]) = bodies = _bodies
}

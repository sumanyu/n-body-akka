package core.universe

import java.util

import core.models.Body
import collection.JavaConverters._


class SystemState {
  private var bodies = IndexedSeq[Body]()

  //Useful inside JavaGUI
  def getJavaBodies: util.List[Body] = bodies.asJava
  def setNewState(_bodies: IndexedSeq[Body]) = bodies = _bodies
}

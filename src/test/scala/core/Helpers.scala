package core

import core.models.Vector2D
import org.scalatest.ShouldMatchers

trait Helpers extends ShouldMatchers {
  private val Tolerance = 0.001
  def closeTo(testValue: Double) = testValue +- Tolerance

  def randomNumber = math.random - 0.5
  def randomVectors: Stream[Vector2D] = Vector2D(randomNumber, randomNumber) #:: randomVectors
}

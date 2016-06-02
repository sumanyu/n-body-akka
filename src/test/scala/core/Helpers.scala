package core

import core.models.Vector2D
import org.scalatest.ShouldMatchers

trait Helpers extends ShouldMatchers {
  private val Tolerance = 0.001
  def closeTo(testValue: Double) = testValue +- Tolerance

  def randomNumber = math.random - 0.5

  def randomVector = Vector2D(randomNumber, randomNumber)
  def randomVectors: Stream[Vector2D] = randomVector #:: randomVectors
  def randomVectorPairs: Stream[(Vector2D, Vector2D)] = randomVectors.zip(randomVectors)
  def randomVectorTriplets: Stream[(Vector2D, Vector2D, Vector2D)] =
    (randomVector, randomVector, randomVector) #:: randomVectorTriplets


}

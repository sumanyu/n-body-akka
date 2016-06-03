package core

import core.models.{Body, Vector2D}
import org.scalatest.ShouldMatchers

trait Helpers extends ShouldMatchers {
  private val Tolerance = 0.01
  def closeTo(testValue: Double) = testValue +- Tolerance

  def randomScalar = math.random - 0.5

  def randomVector = Vector2D(randomScalar, randomScalar)
  def randomVectors: Stream[Vector2D] = randomVector #:: randomVectors
  def randomVectorPairs: Stream[(Vector2D, Vector2D)] = randomVectors.zip(randomVectors)
  def randomVectorTriplets: Stream[(Vector2D, Vector2D, Vector2D)] =
    (randomVector, randomVector, randomVector) #:: randomVectorTriplets

  def randomBody = Body(
    position = randomVector,
    velocity = randomVector,
    force = randomVector,
    mass = randomScalar)
}

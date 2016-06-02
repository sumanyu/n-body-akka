package core.models

import java.awt.Color

import core.universe.UniverseConstants._
import Vector2D._

case class Body(position: Vector2D,
                velocity: Vector2D = Zero,
                force: Vector2D = Zero,
                mass: Double = 0,
                color: Color = Color.BLACK) {

  def +(that: Body): Body = {
    val totalMass = mass + that.mass
    val x = (position.x * mass + that.position.x * that.mass) / totalMass
    val y = (position.y * mass + that.position.y * that.mass) / totalMass

    copy(Vector2D(x, y), Zero, Zero, totalMass)
  }

  def forceBetween(that: Body): Vector2D = {
    val distance = this.position.distance(that.position)
    val squaredDistance = Math.pow(distance, 2)
    val forceMagnitude = (GRAVITATION * that.mass * mass) / squaredDistance
    val unitDirection = (that.position - position) / distance

    unitDirection * forceMagnitude
  }

  def applyForce(_force: Vector2D): Body = copy(force = force + _force)

  def addForceFrom(that: Body): Body = copy(force = force + forceBetween(that))

  def distance(that: Body): Double = this.position.distance(that.position)

  def resetForce: Body = copy(force = Zero)

  def updateStateVariables(): Body = {
    val deltaVelocity = force / mass * DISCRETIZED_TIME_STEP
    val deltaPosition = (velocity + (deltaVelocity / 2.0)) * DISCRETIZED_TIME_STEP
    copy(position = this.position + deltaPosition, velocity = this.velocity + deltaVelocity)
  }
}

package core.models

import java.awt.Color
import core.universe.UniverseConstants._

case class Body(position: Vector2D,
                velocity: Vector2D = Vector2D(0.0, 0.0),
                force: Vector2D = Vector2D(0.0, 0.0),
                mass: Double = 0,
                color: Color) {

  def +(that: Body): Body = {
    val totalMass = mass + that.mass
    val x = (position.x * mass + that.position.x * that.mass) / totalMass
    val y = (position.y * mass + that.position.y * that.mass) / totalMass

    copy(Vector2D(x, y), Vector2D(0, 0), Vector2D(0, 0), totalMass)
  }

  def addForce(that: Body): Body = {
    val distance = this.position.distance(that.position)
    val forceMagnitude = (GRAVITATION * that.mass * mass) / Math.pow(distance, 2)
    val normalizedDirection = (that.position - position) / distance
    val deltaForce = normalizedDirection * forceMagnitude

    copy(force = deltaForce + force)
  }

  def distance(that: Body): Double = {
    this.position.distance(that.position)
  }

  def resetForce: Body = {
    copy(force = Vector2D(0, 0))
  }

  def update: Body = {
    val deltaVelocity = force / mass * DT
    val deltaPosition = (velocity + (deltaVelocity / 2.0)) * DT
    copy(position = this.position + deltaPosition, velocity = this.velocity + deltaVelocity)
  }
}

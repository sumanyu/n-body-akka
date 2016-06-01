package core.models

import java.awt.Color

import core.universe.UniverseConstants._
import Vector2D._

case class Body(position: Vector2D,
                velocity: Vector2D = Zero,
                force: Vector2D = Zero,
                mass: Double = 0,
                color: Color) {

  def +(that: Body): Body = {
    val totalMass = mass + that.mass
    val x = (position.x * mass + that.position.x * that.mass) / totalMass
    val y = (position.y * mass + that.position.y * that.mass) / totalMass

    copy(Vector2D(x, y), Zero, Zero, totalMass)
  }

  //impact of that on this
  def addForce(that: Body): Body = {
    val distance = this.position.distance(that.position)
    val forceMagnitude = (GRAVITATION * that.mass * mass) / Math.pow(distance, 2)
    val direction = that.position - position
    val deltaForce = direction * forceMagnitude / distance

    copy(force = force + deltaForce)
  }

  def distance(that: Body): Double = {
    this.position.distance(that.position)
  }

  def resetForce: Body = {
    copy(force = Zero)
  }

  def updateStateVariables: Body = {
    val deltaVelocity = force / mass * DT
    val deltaPosition = (velocity + (deltaVelocity / 2.0)) * DT
    copy(position = this.position + deltaPosition, velocity = this.velocity + deltaVelocity)
  }
}

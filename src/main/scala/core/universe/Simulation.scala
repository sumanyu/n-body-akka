package core.universe

import java.awt.Color

import core.algorithms.NBodyAlgorithm
import core.models.{Body, Vector2D}
import core.universe.UniverseConstants._
import collection.JavaConverters._

class Simulation(numberOfBodies: Int, nBodyAlgorithm: NBodyAlgorithm) {

  var bodies = IndexedSeq[Body]()

  //Useful inside JavaGUI
  def getJavaBodies = bodies.asJava

  private var executionTime: Long = 0

  initializeBodies()

  /**
    * Initializes the bodies position, velocity and mass.
    *
    * Source: http://physics.princeton.edu/~fpretori/Nbody/intro.htm
    */
  def initializeBodies() {
    bodies +:= generateSun()
    bodies ++= (1 until numberOfBodies).map { i => generateRandomBody() }
  }

  def generateSun() = Body(Vector2D(0, 0), Vector2D(0, 0), Vector2D(0, 0), 1e6 * SOLAR_MASS, Color.RED)

  def generateRandomBody() = {
    val position  = generatePositionVector
    val velocity  = generateVelocity(position)
    val mass      = generateMass
    val color     = generateColor(mass)

    Body(position, velocity, force = Vector2D(0, 0), mass, color)
  }

  def generatePositionVector = Vector2D(generatePosition, generatePosition)
  def generatePosition       = UNIVERSE_RADIUS * exp(-1.8) * (.5 - Math.random)
  def exp(lambda: Double)    = -Math.log(1 - Math.random) / lambda

  def generateVelocity(position: Vector2D) = {
    val mag: Double = circleInitialization(position.x, position.y)
    val absAngle: Double = Math.atan(Math.abs(position.y / position.x))
    val theta: Double = Math.PI / 2 - absAngle

    Vector2D(-1 * Math.signum(position.y) * Math.cos(theta), Math.signum(position.x) * Math.sin(theta)) * mag
  }

  //Helps initialize bodies in circular orbits around a central mass
  def circleInitialization(rx: Double, ry: Double): Double = {
    val r2 = Math.sqrt(rx * rx + ry * ry)
    val numerator = GRAVITATION * 1e6 * SOLAR_MASS
    Math.sqrt(numerator / r2)
  }

  def generateMass = Math.random * SOLAR_MASS * 10 + 1e20

  def generateColor(mass: Double) = {
    def randomColor = Math.floor(mass * 254 / (SOLAR_MASS * 10 + 1e20)).toInt

    val red   = randomColor
    val blue  = randomColor
    val green = 255
    new Color(red, green, blue)
  }

  def simulateOneStep() {
    val deltaTime = time {
      bodies = nBodyAlgorithm.updateBodies(bodies)
    }
    
    println("Execution time: " + deltaTime * 1e-6 + " milliseconds")
  }

  def time(f: => Unit): Long = {
    val t0: Long = System.nanoTime
    f
    System.nanoTime - t0
  }
}

package core.universe

import java.awt.Color

import core.algorithms.NBodyAlgorithm
import core.models.{Body, Vector2D}
import core.universe.UniverseConstants._
import collection.JavaConverters._

class Simulation(numberOfBodies: Int, numberOfSteps: Int, nBodyAlgorithm: NBodyAlgorithm) {

  var bodies = IndexedSeq[Body]()

  private var counter: Int = numberOfSteps
  private var executionTime: Long = 0

  initializeBodies()

  /**
    * Initializes the bodies position, velocity and mass.
    *
    * Source: http://physics.princeton.edu/~fpretori/Nbody/intro.htm
    */
  def initializeBodies() {
    bodies +:= generateSun()

    (0 until numberOfBodies).foreach { i =>
      bodies +:= generateRandomBody()
    }
  }

  def getJavaBodies() = {
    bodies.asJava
  }

  def generateSun() = Body(Vector2D(0, 0), Vector2D(0, 0), Vector2D(0, 0), 1e6 * SOLAR_MASS, Color.RED)

  def generateRandomBody() = {
    val position = Vector2D(generatePosition, generatePosition)
    val velocity = generateVelocity(position)
    val mass: Double = generateMass
    val color: Color = generateColor(mass)

    Body(position, velocity, force = Vector2D(0, 0), mass, color)
  }

  def generateVelocity(position: Vector2D) = {
    val mag: Double = circleInitialization(position.x, position.y)
    val absAngle: Double = Math.atan(Math.abs(position.y / position.x))
    val theta: Double = Math.PI / 2 - absAngle

    Vector2D(-1 * Math.signum(position.y) * Math.cos(theta) * mag,
      Math.signum(position.x) * Math.sin(theta) * mag)
  }

  def generateMass = Math.random * SOLAR_MASS * 10 + 1e20

  def generateColor(mass: Double) = {
    val red: Int = Math.floor(mass * 254 / (SOLAR_MASS * 10 + 1e20)).toInt
    val blue: Int = Math.floor(mass * 254 / (SOLAR_MASS * 10 + 1e20)).toInt
    val green: Int = 255
    new Color(red, green, blue)
  }

  def generatePosition = UNIVERSE_RADIUS * exp(-1.8) * (.5 - Math.random)

  def exp(lambda: Double): Double = {
    -Math.log(1 - Math.random) / lambda
  }

  /**
    * This method will help to initialize the bodies in circular
    * orbits around the central mass.
    *
    * @param rx x coordinate
    * @param ry y coordinate
    * @return position
    */
  def circleInitialization(rx: Double, ry: Double): Double = {
    val r2: Double = Math.sqrt(rx * rx + ry * ry)
    val numerator: Double = GRAVITATION * 1e6 * SOLAR_MASS
    Math.sqrt(numerator / r2)
  }

  /**
    * Runs one simulation step of the simulation.
    */
  def simulate() {
    val deltaTime = time {
      bodies = nBodyAlgorithm.updateBodies(bodies)
    }

    executionTime += deltaTime
    counter -= 1

    if (counter == 0) { //TODO: Graceful shutdown
      println("Number of bodies: " + bodies.size)
      println("Discretized time step for calculation, DT " + DT)
      println("The number of time steps in the simulation: " + numberOfSteps)
      println("Execution time: " + executionTime * 1e-6 + " milliseconds")
      System.exit(1)
    }
  }

  def time(f: => Unit): Long = {
    val t0: Long = System.nanoTime
    f
    System.nanoTime - t0
  }
}

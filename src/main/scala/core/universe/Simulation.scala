package core.universe

import java.awt.Color

import core.models.{BHTree, Body, Quad, Vector2D}
import core.universe.UniverseConstants._

class Simulation(numberOfBodies: Int, numberOfSteps: Int) {

  var bodies = Set[Body]()
  private val q: Quad = Quad(0, 0, 2 * UNIVERSE_RADIUS)

  private var counter: Int = numberOfSteps
  private var executionTime: Long = 0

  initializeBodies()

  /**
    * Initializes the bodies position, velocity and mass.
    *
    * Source: http://physics.princeton.edu/~fpretori/Nbody/intro.htm
    */
  def initializeBodies() {
    bodies += generateSun()

    (0 until numberOfBodies).foreach { i =>
      bodies += generateRandomBody()
    }
  }

  def generateSun() = Body(Vector2D(0, 0), Vector2D(0, 0), Vector2D(0, 0), 1e6 * SOLAR_MASS, Color.RED)

  def generateRandomBody() = {
    val positionX: Double = UNIVERSE_RADIUS * exp(-1.8) * (.5 - Math.random)
    val positionY: Double = UNIVERSE_RADIUS * exp(-1.8) * (.5 - Math.random)
    val magv: Double = circlev(positionX, positionY)
    val absangle: Double = Math.atan(Math.abs(positionY / positionX))
    val thetav: Double = Math.PI / 2 - absangle
    val velocityX: Double = -1 * Math.signum(positionY) * Math.cos(thetav) * magv
    val velocityY: Double = Math.signum(positionX) * Math.sin(thetav) * magv
    val mass: Double = Math.random * SOLAR_MASS * 10 + 1e20
    val red: Int = Math.floor(mass * 254 / (SOLAR_MASS * 10 + 1e20)).toInt
    val blue: Int = Math.floor(mass * 254 / (SOLAR_MASS * 10 + 1e20)).toInt
    val green: Int = 255
    val color: Color = new Color(red, green, blue)
    Body(Vector2D(positionX, positionY), Vector2D(velocityX, velocityY), Vector2D(0, 0), mass, color)
  }

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
  def circlev(rx: Double, ry: Double): Double = {
    val r2: Double = Math.sqrt(rx * rx + ry * ry)
    val numerator: Double = (GRAVITATION) * 1e6 * SOLAR_MASS
    Math.sqrt(numerator / r2)
  }

  /**
    * Runs one simulation step of the simulation.
    */
  def simulate() {
    val a: Long = System.nanoTime
    addForces()
    val b: Long = System.nanoTime - a

    executionTime += b
    counter -= 1

    if (counter == 0) { //TODO: Graceful shutdown
      println("Number of bodies: " + bodies.size)
      println("Discretized time step for calculation, DT " + DT)
      println("The number of time steps in the simulation: " + numberOfSteps)
      println("Execution time: " + executionTime * 1e-6 + " milliseconds")
      System.exit(1)
    }
  }

  /**
    * Barnes Hut algorithm.
    */
  def addForces() {
    val tree: BHTree = new BHTree(q)
    val bodiesInQuad = bodies.filter(q.contains)

    bodiesInQuad.foreach { body =>
      tree.insert(body)
    }

    bodiesInQuad.foreach { body =>
      body.resetForce
      tree.updateForce(body)
      body.update
    }
  }
}
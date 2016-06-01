package core

import javax.swing.JOptionPane

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import com.typesafe.config.ConfigFactory
import core.algorithms.naive.NaiveQuadraticMethod
import core.gui.GUI
import core.http.HttpRoute
import core.universe.Simulation

object Main extends App with HttpRoute {

  val config = ConfigFactory.load()

  implicit val system = ActorSystem("NBody", config)
  implicit val materializer = ActorMaterializer()
  implicit val executionContext = system.dispatcher

  val host = config.getString("http.interface")
  val port = config.getInt("http.port")
  val bindingFuture = Http().bindAndHandle(route, host, port)

  val gnumBodies: Int = JOptionPane.showInputDialog(null, "Enter amount of bodies").toInt - 1
  val numSteps: Int = JOptionPane.showInputDialog(null, "Number of time steps in the simulation: ").toInt

  val naiveQuadraticMethod = new NaiveQuadraticMethod
  val sim: Simulation = new Simulation(gnumBodies, numSteps, naiveQuadraticMethod)
  val gui = new GUI(sim)
}

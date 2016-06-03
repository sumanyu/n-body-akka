package core

import javax.swing.JOptionPane

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import com.typesafe.config.ConfigFactory
import core.algorithms.naive.NaiveQuadraticMethod
import core.gui.JavaGUI
import core.http.HttpRoute
import core.universe.{SimulationActor, SystemState, UniverseConstants}

object Main extends App with HttpRoute {

  val config = ConfigFactory.load()

  implicit val system = ActorSystem("NBody", config)
  implicit val materializer = ActorMaterializer()
  implicit val executionContext = system.dispatcher

  val host = config.getString("http.interface")
  val port = config.getInt("http.port")
  val bindingFuture = Http().bindAndHandle(route, host, port)

  val numBodies = JOptionPane.showInputDialog(null, "Enter amount of bodies").toInt

  val systemState = new SystemState //shared between GUI and processing

  val naiveQuadraticMethod = new NaiveQuadraticMethod
  val simulationActor = system.actorOf(SimulationActor.props(numBodies, naiveQuadraticMethod, systemState))
  val gui = new JavaGUI(systemState, UniverseConstants.UNIVERSE_RADIUS)
}

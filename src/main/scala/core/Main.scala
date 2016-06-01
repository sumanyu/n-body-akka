package core

import javax.swing.JOptionPane

import akka.actor.ActorSystem
import akka.cluster.Cluster
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import com.typesafe.config.ConfigFactory
import core.gui.GUI
import core.http.HttpRoute
import core.universe.Simulation

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.util.Try

object Main extends App with ShutdownHook with HttpRoute {

  val config = ConfigFactory.load()

  implicit val system = ActorSystem("NBody", config)
  implicit val materializer = ActorMaterializer()
  implicit val executionContext = system.dispatcher

  val host = config.getString("http.interface")
  val port = config.getInt("http.port")
  val bindingFuture = Http().bindAndHandle(route, host, port)

  val gnumBodies: Int = JOptionPane.showInputDialog(null, "Enter amount of bodies").toInt - 1
  val numSteps: Int = JOptionPane.showInputDialog(null, "Number of time steps in the simulation: ").toInt
  val sim: Simulation = new Simulation(gnumBodies, numSteps)
  val gui = new GUI(sim)

  addShutdownHook(system)
}

trait ShutdownHook {
  def addShutdownHook(system: ActorSystem) = Cluster(system).registerOnMemberRemoved {
    // exit JVM when ActorSystem has been terminated
    system.registerOnTermination(System.exit(0))
    // shut down ActorSystem
    system.terminate()

    // In case ActorSystem shutdown takes longer than 10 seconds,
    // exit the JVM forcefully anyway.
    // We must spawn a separate thread to not block current thread,
    // since that would have blocked the shutdown of the ActorSystem.
    new Thread {
      override def run(): Unit = {
        if (Try(Await.ready(system.whenTerminated, 10.seconds)).isFailure)
          System.exit(-1)
      }
    }.start()
  }
}
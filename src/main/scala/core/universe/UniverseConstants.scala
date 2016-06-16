package core.universe

object UniverseConstants {
  private val boostedGravitation = 20000
  val GRAVITATION: Double = 6.67e-11 * boostedGravitation
  val DISCRETIZED_TIME_STEP: Double = 1e11
  val SOLAR_MASS: Double = 1.98892e30
  val UNIVERSE_RADIUS: Double = 1e18
  val MIN_MASS = 1e20
}

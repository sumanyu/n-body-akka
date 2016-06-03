package core

import core.models.{Body, Vector2D}

class BodySpec extends BaseSpec {

  "forceBetween" should "give correct force between two masses of body" in {
    val earth = Body(Vector2D.Zero, Vector2D.Zero, Vector2D.Zero, 5.98E24)
    val moon = Body(Vector2D(3.8E8, 0), Vector2D.Zero, Vector2D.Zero, 7.35E22)
    val force = earth.forceBetween(moon).length
    val target = 2.03E20
    val delta = (force - target) / 1.0E18

    delta shouldBe closeTo(0.02)
  }

  it should "" in {

  }

  //TODO tests for other body functions
}

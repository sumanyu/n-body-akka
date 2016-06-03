package core

import core.models.{Body, Vector2D}

class BodySpec extends BaseSpec {

  val earth = Body(Vector2D.Zero, Vector2D.Zero, Vector2D.Zero, 5.98E24)
  val moon = Body(Vector2D(3.8E8, 0), Vector2D.Zero, Vector2D.Zero, 7.35E22)

  "forceBetween" should "give correct force between two masses of body" in {
    val force = earth.forceBetween(moon).length
    val target = 2.03E20
    val delta = (force - target) / 1.0E18

    delta shouldBe closeTo(0.02)
  }

  it should "be symmetric" in {
    val body1 = randomBody
    val body2 = randomBody

    val forceBetweenBody1AndBody2 = body1.forceBetween(body2).length
    val forceBetweenBody2AndBody1 = body2.forceBetween(body1).length

    forceBetweenBody1AndBody2 shouldEqual forceBetweenBody2AndBody1
  }

  "apply force" should "add force to current body and not modify other properties" in {
    val body = randomBody
    val force = randomVector
    val result = body.applyForce(force)

    //invariants
    result.color shouldEqual body.color
    result.mass shouldEqual body.mass
    result.position shouldEqual body.position
    result.velocity shouldEqual body.velocity

    result.force.x shouldBe closeTo(body.force.x + force.x)
    result.force.y shouldBe closeTo(body.force.y + force.y)
  }

  "distance" should "be symmetric between two bodies" in {
    val body1 = randomBody
    val body2 = randomBody

    body1.distance(body2) shouldEqual body2.distance(body1)
  }

  "resetForce" should "only reset force vector to zero and modify no other property" in {
    val body = randomBody
    val result = body.resetForce

    //invariants
    result.color shouldEqual body.color
    result.mass shouldEqual body.mass
    result.position shouldEqual body.position
    result.velocity shouldEqual body.velocity

    result.force shouldEqual Vector2D.Zero
  }

  "setting zero mass for body" should "throw exception" in {
    intercept[IllegalArgumentException] {
      Body(mass = 0)
    }
  }
}

package core

import core.algorithms.NBodyAlgorithm
import core.algorithms.naive.NaiveQuadraticMethod
import core.models.{Vector2D, Body}

class NaiveQuadraticMethodSpec extends BaseSpec {
  val updateAlgorithm: NBodyAlgorithm = new NaiveQuadraticMethod

  "NaiveQuadraticMethod" should "not change properties of body for a stationary body" in {
    val body = Body()
    val bodies = IndexedSeq(body)
    val updatedBody = updateAlgorithm.updateBodies(bodies).head

    body shouldEqual updatedBody
  }

  it should "for two stationary bodies update the bodies to move towards each other" in {
    val body1 = Body(position = Vector2D(0, 0), mass = 100)
    val body2 = Body(position = Vector2D(1000000, 0), mass = 100)

    val bodies = IndexedSeq(body1, body2)
    val updatedBody = updateAlgorithm.updateBodies(bodies)

    val resultBody1 = updatedBody.head
    val resultBody2 = updatedBody.last

    resultBody1.position.x shouldBe > (0.0)
    resultBody2.position.x shouldBe < (1000000.0)
  }
}

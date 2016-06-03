package core

import core.models.Vector2D

class Vector2DSpec extends BaseSpec {

  "length" should "be zero for zero vector" in {
    Vector2D.Zero.length shouldBe closeTo(0.0)
  }

  it should "be correct for an arbitrary vector" in {
    Vector2D(8, 6).length shouldBe closeTo(10)
  }

  it should "be symmetric in x and y" in {
    randomVectors.take(5).foreach { v =>
      Vector2D(v.y, v.x).length shouldBe closeTo(v.length)
    }
  }

  it should "be invariant to reflections about x = y" in {
    randomVectors.take(5).foreach { v =>
      (v * -1).length shouldBe closeTo(v.length)
    }
  }

  "distance" should "be symmetric" in {
    randomVectorPairs.take(5).foreach { pair =>
      val v1 = pair._1
      val v2 = pair._2

      v1.distance(v2) shouldBe closeTo(v2.distance(v1))
    }
  }

  it should "be non-negative" in {
    randomVectorPairs.take(5).foreach { pair =>
      val v1 = pair._1
      val v2 = pair._2

      v1.distance(v2) should be >= 0.0
    }
  }

  it should "be zero for distance between itself" in {
    randomVectors.take(5).foreach { v =>
      v.distance(v) shouldEqual 0.0
    }
  }

  it should "preserve triangle inequality" in {
    randomVectorTriplets.take(5).foreach { case (x, y, z) =>
      x.distance(z) should be <= (x.distance(y) + y.distance(z))
    }
  }

  "vector addition" should "add vectors component wise" in {
    val v1 = Vector2D(4, 5)
    val v2 = Vector2D(2, 3)
    val target = Vector2D(6, 8)

    (v1 + v2) shouldEqual target
  }

  it should "satisfy commutative property" in {
    val v1 = randomVector
    val v2 = randomVector

    (v1 + v2) shouldEqual v2 + v1
  }

  it should "satisfy associative property" in {
    val v1 = randomVector
    val v2 = randomVector
    val v3 = randomVector

    ((v1 + v2) + v3) shouldEqual (v1 + (v2 + v3))
  }

  it should "satisfy additive identity" in {
    val v1 = randomVector

    v1 + Vector2D.Zero shouldEqual v1
    Vector2D.Zero + v1 shouldEqual v1
  }

  it should "satisfy inverse additive property" in {
    val v1 = randomVector
    val v1_minus = v1 * -1

    v1 + v1_minus shouldEqual Vector2D.Zero
  }

  it should "satisfy distributed property" in {
    val v1 = randomVector
    val v2 = randomVector
    val s = randomScalar

    val result = (v1 + v2) * s
    val target = v1*s + v2*s

    result.x shouldBe closeTo (target.x)
    result.y shouldBe closeTo (target.y)
  }

  it should "satisfy multiplicative property" in {
    val v1 = randomVector

    (v1 * 1) shouldEqual v1
  }
}

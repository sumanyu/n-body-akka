package core

import core.models.Vector2D

class Vector2DSpec extends BaseSpec {

  "length" should "be zero for zero complex number" in {
    Vector2D(0, 0).length shouldBe closeTo(0.0)
  }

  it should "be correct for an arbitrary complex number" in {
    Vector2D(8, 6).length shouldBe closeTo(10)
  }

  it should "be symmetric in re and im" in {
    randomVectors.take(5).foreach { v =>
      Vector2D(v.y, v.x).length shouldBe closeTo(v.length)
    }
  }

  it should "be invariant to reflections about x = y" in {
    randomVectors.take(5).foreach { v =>
      (v * -1).length shouldBe closeTo(v.length)
    }
  }
}

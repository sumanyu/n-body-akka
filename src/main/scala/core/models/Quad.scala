package core.models

case class Quad(midpoint: Vector2D, sideLength: Double) {

  private def halfSideLength = sideLength / 2.0

  def contains(that: Vector2D): Boolean = {
    val halfSide = halfSideLength

    that.x <= midpoint.x + halfSide &&
      that.x >= midpoint.x - halfSide &&
      that.y <= midpoint.y + halfSide &&
      that.y >= midpoint.y - halfSide
  }

  def contains(that: Body): Boolean = contains(that.position)

  def northWest = Quad(Vector2D(midpoint.x - sideLength, midpoint.y + sideLength) / 4.0, halfSideLength)
  def northEast = Quad(Vector2D(midpoint.x + sideLength, midpoint.y + sideLength) / 4.0, halfSideLength)
  def southWest = Quad(Vector2D(midpoint.x - sideLength, midpoint.y - sideLength) / 4.0, halfSideLength)
  def southEast = Quad(Vector2D(midpoint.x + sideLength, midpoint.y - sideLength) / 4.0, halfSideLength)
}

object Quad {
  def apply(x: Int, y: Int, sideLength: Double): Quad = Quad(Vector2D(x, y), sideLength)
}
package core.models

case class Vector2D(x: Double, y: Double) extends {
  def length: Double = math.sqrt(x*x + y*y)
  def +(that: Vector2D): Vector2D = copy(x + that.x, y + that.y)
  def -(that: Vector2D): Vector2D = copy(x - that.x, y - that.y)
  def *(that: Double): Vector2D = copy(x * that, y * that)
  def /(that: Double): Vector2D = copy(x / that, y / that)
  def distance(that: Vector2D) = (this - that).length

  override def toString() = s"(${x}, ${y})"
}

object Vector2D {
  val Zero = Vector2D(0, 0)
}
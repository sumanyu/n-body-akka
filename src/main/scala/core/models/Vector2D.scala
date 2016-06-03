package core.models


case class Vector2D(x: Double, y: Double) extends {

  def +(that: Vector2D): Vector2D = copy(x + that.x, y + that.y)
  def -(that: Vector2D): Vector2D = this + (that * -1)

  def *(scalar: Double): Vector2D   = copy(x * scalar, y * scalar)
  def /(scalar: Double): Vector2D   = this * (1.0 / scalar)

  def distance(that: Vector2D)    = (this - that).length
  def length: Double              = math.sqrt(x*x + y*y)

  override def toString           = s"($x, $y)"
}

object Vector2D {
  type VectorType = Double
  val Zero = Vector2D(0, 0)
}
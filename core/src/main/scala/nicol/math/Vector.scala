package nicol.math

sealed case class Vector(x: Float, y: Float) extends Immutable {
  def +(v: Vector) = Vector(x + v.x, y + v.y)

  def -(v: Vector) = Vector(x - v.x, y - v.y)

  lazy val unary_- = Vector(-x, -y)

  def *(v: Vector) = x * v.x + y * v.y

  def *(f: Float) = Vector(x * f, y * f)

  def *(m: Matrix) = Vector(m.a * x + m.c * y, m.b * x + m.d * y)

  def /(f: Float) = Vector(x / f, y / f)

  lazy val length = math.sqrt(lengthSqr.toDouble).toFloat

  val lengthSqr = x * x + y * y

  def angle(that: Vector) = math.atan2(this.y, this.x).toFloat - math.atan2(that.y, that.x).toFloat

  lazy val normalised = this * (1 / length)

  override def toString = "(" + x + ", " + y + ")"
}

object Vector {
  def apply(t: (Float, Float)): Vector = Vector(t._1, t._2)

  implicit def asTuple(v: Vector) = (v.x, v.y)

  implicit def asVector(t: (Float, Float)) = Vector(t._1, t._2)

  implicit def intAsVector(t: (Int, Int)) = Vector(t._1.toFloat, t._2.toFloat)

  val zero = Vector(0, 0)

  val up = Vector(0, -1)

  val down = Vector(0, 1)

  val left = Vector(-1, 0)

  val right = Vector(1, 0)

}
package nicol.math

sealed case class Vector(x: Float, y: Float) extends Immutable {
  def +(v: Vector) = Vector(x + v.x, y + v.y)

  def -(v: Vector) = Vector(x - v.x, y - v.y)

  def unary_- = Vector(-x, -y)

  def *(v: Vector) = x * v.x + y * v.y

  def *(f: Float) = Vector(x * f, y * f)

  def *(m: Matrix) = Vector(m.a * x + m.c * y, m.b * x + m.d * y)

  def /(f: Float) = Vector(x / f, y / f)

  def length = math.sqrt(x * x + y * y).toFloat

  def lengthSqr = x * x + y * y

  def angle(v: Vector) = math.acos(this * v).toFloat

  def normalised = {
    val l = length
    if (l != 0) this / l
    else Vector.zero
  }

  override def toString = "(" + x + ", " + y + ")"
}

object Vector {
  def apply(t: (Float, Float)): Vector = Vector(t._1, t._2)

  implicit def asTuple(v: Vector) = (v.x, v.y)

  implicit def asVector(t: (Float, Float)) = Vector(t._1, t._2)

  implicit def intAsVector(t: (Int, Int)) = Vector(t._1.toFloat, t._2.toFloat)

  object zero extends Vector(0, 0)

  object up extends Vector(0, 1)

  object down extends Vector(0, -1)

  object left extends Vector(-1, 0)

  object right extends Vector(1, 0)

}
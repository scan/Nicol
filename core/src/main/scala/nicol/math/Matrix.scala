package nicol.math

/// @todo Turn these into traits to support other kind of matrices

/**
 * A 2x2 Matrix, stored in Row-Major order:
 *
 * ( a b )
 * ( c d )
 */
sealed class Matrix private(val a: Float, val b: Float, val c: Float, val d: Float) extends Immutable {
  lazy val transposed = Matrix((a, b), (c, d))

  @throws(classOf[ArithmeticException])
  lazy val invert = new Matrix(d, -b, -c, a) * (1f / (a * d - b * c))

  def *(m: Matrix) = new Matrix(a * m.a + b * m.c, a * m.b + b * m.d, c * m.a + d * m.c, c * m.b + d * m.d)

  def +(m: Matrix) = new Matrix(a + m.a, b + m.b, c + m.c, d + m.d)

  def -(m: Matrix) = new Matrix(a - m.a, b - m.b, c - m.c, d - m.d)

  def *(f: Float) = new Matrix(f * a, f * b, f * c, f * d)

  def *(v: Vector) = Vector(a * v.x + c * v.y, b * v.x + d * v.y)
}

object Matrix {

  object identity extends Matrix(1, 0, 0, 1)

  case class rotated(r: Float) extends Matrix(
    math.cos(r).toFloat,
    -math.sin(r).toFloat,
    math.sin(r).toFloat,
    math.cos(r).toFloat
  )

  case class scaled(sx: Float, sy: Float) extends Matrix(sx, 0, 0, sy)

  /**
   * Create a new Matrix from Column-Major tuples
   *
   * ( t1.x t2.x )
   * ( t1.y t2.y )
   */
 def apply(t1: (Float, Float), t2: (Float, Float)): Matrix = new Matrix(t1._1, t2._1, t1._2, t2._2)

  /**
   * Create a new Matrix from Row-Major values
   *
   * ( a b )
   * ( c d )
   */
  def apply(a: Float, b: Float, c: Float, d: Float): Matrix = new Matrix(a, b, c, d)
}
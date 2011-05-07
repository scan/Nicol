package com.scan.nicol.math

import scala.annotation._

sealed case class Matrix(a: Float, b: Float, c: Float, d: Float) extends Immutable {
  def transposed = Matrix((a, b), (c, d))

  @throws(classOf[ArithmeticException])
  def invert = Matrix(d, -b, -c, a) * (1f / (a * d - b * c))

  def *(m: Matrix) = Matrix(a * m.a + b * m.c, a * m.b + b * m.d, c * m.a + d * m.c, c * m.b + d * m.d)

  def +(m: Matrix) = Matrix(a + m.a, b + m.b, c + m.c, d + m.d)

  def -(m: Matrix) = Matrix(a - m.a, b - m.b, c - m.c, d - m.d)

  def *(f: Float) = Matrix(f * a, f * b, f * c, f * d)

  def *(v: Vector) = Vector(a * v.x + c * v.y, b * v.x + d * v.y)
}

object Matrix {

  object identity extends Matrix(1, 0, 0, 1)

  // TODO: perhaps make these methods?
  object rotated {
    def apply(amount: Float) = Matrix (
      math.cos(amount).toFloat,
      math.sin(amount).toFloat * -1,
      math.sin(amount).toFloat,
      math.cos(amount).toFloat
    )
  }

  // TODO: perhaps make these methods?
  object scaled {
    def apply(sx: Float, sy: Float) = Matrix(sx, 0, 0, sy)
  }

  def apply(t1: (Float, Float), t2: (Float, Float)): Matrix = Matrix(t1._1, t2._1, t1._2, t2._2)
}

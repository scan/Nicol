package com.scan.nicol.math

sealed case class Matrix(a: Float, b: Float, c: Float, d: Float) extends Immutable {
  def transpose = Matrix((a, b), (c, d))

  @throws(classOf[ArithmeticException])
  def invert = Matrix(d, -b, -c, a) * (1f / (a * d - b * c))

  def *(m: Matrix) = Matrix(a * m.a + b * m.c, a * m.b + b * m.d, c * m.a + d * m.c, c * m.b + d * m.d)

  def +(m: Matrix) = Matrix(a + m.a, b + m.b, c + m.c, d + m.d)

  def -(m: Matrix) = Matrix(a - m.a, b - m.b, c - m.c, d - m.d)

  def *(f: Float) = Matrix(f * a, f * b, f * c, f * d)

  def *(v: Vector2) = Vector2(a * v.x + c * v.y, b * v.x + d * v.y)
}

object Matrix {
  def identity = Matrix((1, 0), (0, 1))

  def apply(t1: (Float, Float), t2: (Float, Float)): Matrix = Matrix(t1._1, t2._1, t1._2, t2._2)

  def apply(a: Float): Matrix = {
    val c = math.cos(a).toFloat
    val s = math.sin(a).toFloat
    Matrix((c, s), (-s, c))
  }
}
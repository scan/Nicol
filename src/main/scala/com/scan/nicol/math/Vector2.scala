package com.scan.nicol.math

sealed case class Vector2(x: Float, y: Float) extends Immutable {
  def +(v: Vector2) = Vector2(x + v.x, y + v.y)

  def -(v: Vector2) = Vector2(x - v.x, y - v.y)

  def *(v: Vector2) = x * v.x + y * v.y

  def *(f: Float) = Vector2(x * f, y * f)

  def /(f: Float) = Vector2(x / f, y / f)

  def length = math.sqrt(x * x + y * y).toFloat

  def lengthSqr = x * x + y * y

  def angle(v: Vector2) = math.acos(this * v).toFloat

  def normalised = {
    val l = length
    if (l != 0) this / l
    else Vector2.zero
  }
}

object Vector2 {
  def apply(t: (Float, Float)): Vector2 = Vector2(t._1, t._2)

  implicit def asTuple(v: Vector2) = (v.x, v.y)

  implicit def asVector2(t: (Float, Float)) = Vector2(t._1, t._2)

  def zero = Vector2(0, 0)
}
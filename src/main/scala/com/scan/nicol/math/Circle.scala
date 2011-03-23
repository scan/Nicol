package com.scan.nicol.math

case class Circle(center: (Float, Float), radius: Float) extends Immutable {
  def x = center._1

  def y = center._2

  private val r2 = radius * radius

  def *(f: Float) = Circle(center, radius * f)

  def collides(v: (Float, Float)) = (Vector2(v) - center).lengthSqr < r2

  def collides(c: Circle) = (Vector2(c.center) - center).lengthSqr < (r2 + c.r2)
}
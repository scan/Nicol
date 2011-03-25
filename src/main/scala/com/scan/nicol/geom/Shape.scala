package com.scan.nicol.geom

import com.scan.nicol.math.Vector2

sealed trait Shape extends Immutable {
  def center: Vector2

  def pos: Vector2

  def area: Float
}

case class Point(pos: Vector2) extends Shape {
  def center = pos

  def area = 0
}

case class Circle(center: Vector2, r: Float) extends Shape {
  def pos = center

  def *(f: Float) = Circle(center, r * f)

  def area = scala.math.Pi.toFloat * r * r
}
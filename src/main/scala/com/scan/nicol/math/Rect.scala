package com.scan.nicol.math

case class Rect(x: Int, y: Int, width: Int, height: Int) extends Immutable {
  lazy val center = (x + width / 2, y + height / 2)

  def pos = (x, y)

  def left = x

  def top = y

  def right = x + width

  def bottom = y + height

  def collides(r: Rect) = (right > r.left || left < r.right) && (bottom > r.top || top < r.bottom)

  def collides(t: (Int, Int)) = (t._1 > left && t._1 < right) && (t._2 > top && t._2 < bottom)

  def +(r: Rect) = {
    import math.{min, max}

    val nx = min(x, r.x)
    val ny = min(y, r.y)

    Rect(nx, ny, max(right - nx, r.right - nx), max(bottom - ny, r.bottom - ny))
  }

  def normalise = {
    import scala.math.abs
    Rect(if (width < 0) x + width else x,
      if (height < 0) y + height else y,
      abs(width), abs(height))
  }

  override def toString = "[(" + left + ", " + top + "), (" + right + ", " + bottom + ")]"
}

object Rect {
  implicit def asTuple4(r: Rect) = (r.x, r.y, r.width, r.right)

  implicit def fromTuple4(t: (Int, Int, Int, Int)) = Rect(t._1, t._2, t._3, t._4)
}
package com.scan.nicol.math

case class Rect(x: Int, y: Int, width: Int, height: Int) extends Immutable {
  def center = (x + width / 2, y + height / 2)

  def pos = (x, y)

  def left = x

  def top = y

  def right = x + width

  def bottom = y + height

  def collides(r: Rect) = (right > r.left || left < r.right) && (bottom > r.top || top < r.bottom)

  def collides(t: (Int, Int)) = (t._1 > left && t._1 < right) && (t._2 > top && t._2 < bottom)
}
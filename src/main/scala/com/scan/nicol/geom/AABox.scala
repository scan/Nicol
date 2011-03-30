package com.scan.nicol.geom

/**
 * An axis-aligned box for simple collision detection.
 */
sealed class AABox(x: Float, y: Float, width: Float, height: Float) extends Immutable {
  def overlaps(that: AABox) = ((this.width + that.width) / 2 > math.abs(this.x - that.x)) && ((this.height + that.height) / 2 > math.abs(this.y - that.y))

  def transposed(dx: Float, dy: Float) = new AABox(x + dx, y + dx, width, height)

  override def toString = "[AABox: " + width + " by " + height + "]"
}

object AABox {
  def apply(x: Float, y: Float, width: Float, height: Float): AABox = new AABox(x, y, width, height)

  def apply(width: Float, height: Float): AABox = new AABox(0, 0, width, height)
}
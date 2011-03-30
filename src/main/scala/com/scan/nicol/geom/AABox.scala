package com.scan.nicol.geom

/**
 * An axis-aligned box for simple collision detection.
 */
sealed case class AABox(x: Float, y: Float, width: Float, height: Float) {
  def overlaps(that: AABox) = ((this.width + that.width) / 2 > math.abs(this.x - that.x)) && ((this.height + that.height) / 2 > math.abs(this.y - that.y))

  def transposed(dx: Float, dy: Float) = AABox(x + dx, y + dx, width, height)

  override def toString = "[AABox: " + width + " by " + height + "]"
}

object AABox {
  def apply(width: Float, height: Float): AABox = AABox(0, 0, width, height)
}
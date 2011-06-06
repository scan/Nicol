package nicol.geom

import nicol.math.Rect

/**
 * An axis-aligned box for simple collision detection.
 */
sealed class AABox(val x: Float, val y: Float, val width: Float, val height: Float) extends Immutable {
  def overlaps(that: AABox) = ((this.width + that.width) / 2 > math.abs(this.x - that.x)) && ((this.height + that.height) / 2 > math.abs(this.y - that.y))

  def intersects(that:AABox) = !(this.bottom < that.top) && !(this.top > that.bottom) && !(this.right < that.left) && !(this.left > that.right)

  def top = y

  def bottom = y + height

  def left = x

  def right = x + width

  def transposed(dx: Float, dy: Float) = new AABox(x + dx, y + dx, width, height)

  override def toString = "[AABox: " + width + " by " + height + "]"
}

object AABox {
  def apply(x: Float, y: Float, width: Float, height: Float): AABox = new AABox(x, y, width, height)

  def apply(width: Float, height: Float): AABox = new AABox(0, 0, width, height)

  implicit def asRect(that: AABox) = Rect(that.x.toInt, that.y.toInt, that.width.toInt, that.height.toInt)

  implicit def fromRect(that: Rect) = AABox(that.x.toFloat, that.y.toFloat, that.width.toFloat, that.height.toFloat)
}

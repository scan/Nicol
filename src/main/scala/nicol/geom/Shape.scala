package nicol
package geom

import math.{Vector, Rect, Matrix}
import scala.math.{abs, sin, cos}

sealed trait Shape extends Immutable {
  /**
   * General bounds of this Shape.
   *
   * @note This is only useful for general collision testing, not exact tests.
   */
  def bounds: AABox

  /**
   * The area of this Shape. Could be useful to someone.
   */
  def area: Float = 0

  /**
   * Same shape, somewhere else.
   */
  def transposed(v: Vector): Shape
}

/**
 * An axis-aligned box for simple collision detection.
 */
sealed class AABox(val x: Float, val y: Float, val width: Float, val height: Float) extends Shape {
  def overlaps(that: AABox) = ((this.width + that.width) / 2 > abs(this.x - that.x)) && ((this.height + that.height) / 2 > abs(this.y - that.y))

  def intersects(that: AABox) = !(this.bottom < that.top) && !(this.top > that.bottom) && !(this.right < that.left) && !(this.left > that.right)

  def top = y

  def bottom = y + height

  def left = x

  def right = x + width

  def transposed(v: Vector) = new AABox(x + v.x, y + v.y, width, height)

  def transposed(dx: Float, dy: Float) = new AABox(x + dx, y + dy, width, height)

  override def toString = "[AABox: " + width + " by " + height + "]"

  def bounds = this

  override def area = (width * height).toFloat
}

object AABox {
  def apply(x: Float, y: Float, width: Float, height: Float): AABox = new AABox(x, y, width, height)

  def apply(width: Float, height: Float): AABox = new AABox(0, 0, width, height)

  implicit def asRect(that: AABox) = Rect(that.x.toInt, that.y.toInt, that.width.toInt, that.height.toInt)

  implicit def fromRect(that: Rect) = AABox(that.x.toFloat, that.y.toFloat, that.width.toFloat, that.height.toFloat)
}

/**
 * This is a straight, not-interrupted line, in the mathematical and
 * graphical sense.
 */
case class Line(start: Vector, end: Vector) extends Shape {
  /**
   * This line as a [[nicol.math.Vector]]
   */
  val vec = end - start

  lazy val bounds = {
    val r = scala.math.max(start.length, end.length)
    AABox(r * 2, r * 2)
  }

  override def toString = "[Line: " + start + " to " + end + "]"

  def transposed(v: Vector) = Line(start + v, end + v)

  def transform(m: Matrix) = Line(m * start, m * end)

  /**
   * Tests if this line intersects with a given line.
   *
   * @param that Line to test
   * @return None, if there was no intersection, Some[Vector] with the point where they intersect
   * @note This is as untested as it is complex.
   */
  def intersects(that: Line) = {
    val d1 = end - start
    val d2 = that.end - that.start
    val d = (d2.y * d1.x) - (d2.x * d1.y)

    if (d == 0) None
    else {
      val f1 = (start.x * end.y - start.y * end.x)
      val f2 = (that.start.x * that.end.y - that.start.y * that.end.x)
      val vx = f1 * (that.start.x - that.end.x) - (start.x - end.x) * f2
      val vy = f1 * (that.start.y - that.end.y) - (start.y - end.y) * f2
      Some(Vector(vx / d, vy / d))
    }
  }
}

/**
 * Circle shape, in strict mathematical sense. Graphical rendering may vary.
 */
case class Circle(center: Vector, radius: Float) extends Shape {
  val r2 = radius

  override def area = scala.math.Pi.toFloat * r2

  def transposed(v: Vector) = Circle(center + v, radius)

  def bounds = AABox(center.x - radius, center.y - radius, radius * 2, radius * 2)

  def intersects(that: Circle) = (that.center - this.center).lengthSqr < (r2 + that.r2)
}

/**
 * A Shape with four edges without any restrictions.
 */
case class Quad(p1: Vector, p2: Vector, p3: Vector, p4: Vector) extends Shape {
  def transposed(v: Vector) = Quad(p1 + v, p2 + v, p3 + v, p4 + v)

  def bounds = AABox(min_x, min_y, max_x - min_x, max_y - min_y)

  private lazy val (min_x, max_x) = {
    val tmp = Seq(p1.x, p2.x, p3.x, p4.x)
    (tmp.min, tmp.max)
  }

  private lazy val (min_y, max_y) = {
    val tmp = Seq(p1.y, p2.y, p3.y, p4.y)
    (tmp.min, tmp.max)
  }

  /**
   * Minimal point
   *
   * @note may or may not be inside the Shape.
   */
  val min = (min_x, min_y)

  /**
   * Maximal point
   *
   * @note May or may not be inside the Shape.
   */
  val max = (min_x, min_y)
}
package com.scan.nicol
package geom

import math._

sealed trait Shape extends Immutable {
  def bounds: AABox

  def area: Float = 0

  def transposed(v: Vector): Shape
}

object Shape {

  import opengl._
  import GLUtils._

  implicit object LineRenderer extends Renderer[Line] {
    def draw(that: Line, x: Float, y: Float, rgb: (Float, Float, Float)) = withoutTextures {
      GLUtils.draw(Lines) {
        colour(rgb._1, rgb._2, rgb._3)
        vertex(that.start.x + x, that.start.y + y)
        vertex(that.end.x + x, that.end.y + y)
      }
    }
  }

  implicit object CircleRenderer extends Renderer[Circle] {
    private val sections = 24
    private val angles = for (a <- 0 to sections) yield a * ((2 * scala.math.Pi.toFloat) / sections)

    import scala.math.{cos, sin}

    def draw(that: Circle, x: Float, y: Float, rgb: (Float, Float, Float)) = preserve(withoutTextures {
      translate(that.center.x + x, that.center.y + y)
      GLUtils.draw(Lines) {
        colour(rgb._1, rgb._2, rgb._3)
        val r = that.radius
        for (i <- 0 until angles.length - 1) {
          vertex(r * cos(angles(i)).toFloat, r * sin(angles(i)).toFloat)
          vertex(r * cos(angles(i + 1)).toFloat, r * sin(angles(i + 1)).toFloat)
        }

      }
    })
  }

  implicit object AABoxRenderer extends Renderer[AABox] {
    def draw(that: AABox, x: Float, y: Float, rgb: (Float, Float, Float)) = preserve(withoutTextures {
      translate(x, y)
      GLUtils.draw(Quads) {
        colour(rgb._1, rgb._2, rgb._3)
        vertex(that.left, that.top)
        vertex(that.right, that.top)
        vertex(that.right, that.bottom)
        vertex(that.left, that.bottom)
      }
    })
  }

}

case class Line(start: Vector, end: Vector) extends Shape {
  /**
   * This line as a [Vector]
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

case class Circle(center: Vector, radius: Float) extends Shape {
  override def area = scala.math.Pi.toFloat * radius * radius

  def transposed(v: Vector) = Circle(center + v, radius)

  def bounds = AABox(center.x - radius, center.y - radius, radius * 2, radius * 2)

  def intersects(that: Circle) = (that.center - this.center).lengthSqr < (radius * radius + that.radius * that.radius)
}
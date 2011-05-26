package nicol
package renderer

import opengl.GLUtils
import geom.{Quad, AABox, Circle, Line}
import GLUtils._

trait StandardGeometryRenderer {
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
    private val angles = for (a <- 0 until sections) yield a * ((2 * scala.math.Pi.toFloat) / sections)

    import scala.math.{cos, sin}

    def draw(that: Circle, x: Float, y: Float, rgb: (Float, Float, Float)) = preserve(withoutTextures {
      translate(that.center.x + x, that.center.y + y)
      GLUtils.draw(LineLoop) {
        colour(rgb._1, rgb._2, rgb._3)
        val r = that.radius
        angles.foreach {
          a =>
            vertex(r * cos(a).toFloat, r * sin(a).toFloat)
        }
      }
    })
  }

  implicit object AABoxRenderer extends Renderer[AABox] {
    def draw(that: AABox, x: Float, y: Float, rgb: (Float, Float, Float)) = preserve(withoutTextures {
      translate(x, y)
      GLUtils.draw(LineLoop) {
        colour(rgb._1, rgb._2, rgb._3)
        vertex(that.left, that.top)
        vertex(that.right, that.top)
        vertex(that.right, that.bottom)
        vertex(that.left, that.bottom)
      }
    })
  }

  implicit object QuadRenderer extends Renderer[Quad] {
    def draw(that: Quad, x: Float, y: Float, rgb: (Float, Float, Float)) = preserve(withoutTextures {
      translate(x, y)
      GLUtils.draw(LineLoop) {
        colour(rgb._1, rgb._2, rgb._3)
        vertex(that.p1.x, that.p1.y)
        vertex(that.p2.x, that.p2.y)
        vertex(that.p3.x, that.p3.y)
        vertex(that.p4.x, that.p4.y)
      }
    })
  }

  object FilledQuadRenderer extends Renderer[Quad] {
    def draw(that: Quad, x: Float, y: Float, rgb: (Float, Float, Float)) = preserve(withoutTextures {
      translate(x, y)
      GLUtils.draw(Quads) {
        colour(rgb._1, rgb._2, rgb._3)
        vertex(that.p1.x, that.p1.y)
        vertex(that.p2.x, that.p2.y)
        vertex(that.p3.x, that.p3.y)
        vertex(that.p4.x, that.p4.y)
      }
    })
  }

  object FilledCircleRenderer extends Renderer[Circle] {
    private val sections = 24
    private val angles = for (a <- 0 to sections) yield a * ((2 * scala.math.Pi.toFloat) / sections)

    import scala.math.{cos, sin}

    def draw(that: Circle, x: Float, y: Float, rgb: (Float, Float, Float)) = preserve(withoutTextures {
      translate(that.center.x + x, that.center.y + y)
      GLUtils.draw(Polygon) {
        colour(rgb._1, rgb._2, rgb._3)
        val r = that.radius
        for (i <- 0 until angles.length - 1) {
          vertex(r * cos(angles(i)).toFloat, r * sin(angles(i)).toFloat)
          vertex(r * cos(angles(i + 1)).toFloat, r * sin(angles(i + 1)).toFloat)
        }
      }
    })
  }

  object FilledAABoxRenderer extends Renderer[AABox] {
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

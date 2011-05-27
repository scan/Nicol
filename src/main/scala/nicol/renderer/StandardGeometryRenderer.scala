package nicol
package renderer

import opengl.GLUtils
import geom.{Quad, AABox, Circle, Line}
import GLUtils._

trait StandardGeometryRenderer {

  implicit object LineRenderer extends Renderer[Line] {
    def draw(that: Line, position: (Float, Float), rgb: (Float, Float, Float), rotation: Float, offset: (Float, Float)) =
      stdDraw(position, rotation, offset)(GLUtils.draw(Lines) {
        colour(rgb._1, rgb._2, rgb._3)
        vertex(that.start.x, that.start.y)
        vertex(that.end.x, that.end.y)
      })
  }

  implicit object CircleRenderer extends Renderer[Circle] {
    private val sections = 24
    private val angles = for (a <- 0 until sections) yield a * ((2 * scala.math.Pi.toFloat) / sections)

    import scala.math.{cos, sin}

    def draw(that: Circle, position: (Float, Float), rgb: (Float, Float, Float), rotation: Float, offset: (Float, Float)) =
      stdDraw(position, rotation, offset)(GLUtils.draw(LineLoop) {
        colour(rgb._1, rgb._2, rgb._3)
        angles.foreach(a => vertex(that.center.x+that.radius * cos(a).toFloat, that.center.y+that.radius * sin(a).toFloat))
      })
  }

  implicit object AABoxRenderer extends Renderer[AABox] {
    def draw(that: AABox, position: (Float, Float), rgb: (Float, Float, Float), rotation: Float, offset: (Float, Float)) = stdDraw(position, rotation, offset) {
      GLUtils.draw(LineLoop) {
        colour(rgb._1, rgb._2, rgb._3)
        vertex(that.left, that.top)
        vertex(that.right, that.top)
        vertex(that.right, that.bottom)
        vertex(that.left, that.bottom)
      }
    }
  }

  implicit object QuadRenderer extends Renderer[Quad] {
    def draw(that: Quad, position: (Float, Float), rgb: (Float, Float, Float), rotation: Float, offset: (Float, Float)) =
      stdDraw(position, rotation, offset)(GLUtils.draw(LineLoop) {
        colour(rgb._1, rgb._2, rgb._3)
        vertex(that.p1.x, that.p1.y)
        vertex(that.p2.x, that.p2.y)
        vertex(that.p3.x, that.p3.y)
        vertex(that.p4.x, that.p4.y)
      })
  }

  object FilledQuadRenderer extends Renderer[Quad] {
    def draw(that: Quad, position: (Float, Float), rgb: (Float, Float, Float), rotation: Float, offset: (Float, Float)) =
      stdDraw(position, rotation, offset)(GLUtils.draw(Quads) {
        colour(rgb._1, rgb._2, rgb._3)
        vertex(that.p1.x, that.p1.y)
        vertex(that.p2.x, that.p2.y)
        vertex(that.p3.x, that.p3.y)
        vertex(that.p4.x, that.p4.y)
      })
  }

  object FilledCircleRenderer extends Renderer[Circle] {
    private val sections = 24
    private val angles = for (a <- 0 to sections) yield a * ((2 * scala.math.Pi.toFloat) / sections)

    import scala.math.{cos, sin}

    def draw(that: Circle, position: (Float, Float), rgb: (Float, Float, Float), rotation: Float, offset: (Float, Float)) =
      stdDraw(position, rotation, offset)(GLUtils.draw(Polygon) {
        colour(rgb._1, rgb._2, rgb._3)
        angles.foreach(a => vertex(that.center.x+that.radius * cos(a).toFloat, that.center.y+that.radius * sin(a).toFloat))
      })
  }

  object FilledAABoxRenderer extends Renderer[AABox] {
    def draw(that: AABox, position: (Float, Float), rgb: (Float, Float, Float), rotation: Float, offset: (Float, Float)) =
      stdDraw(position, rotation, offset)(GLUtils.draw(Quads) {
        colour(rgb._1, rgb._2, rgb._3)
        vertex(that.left, that.top)
        vertex(that.right, that.top)
        vertex(that.right, that.bottom)
        vertex(that.left, that.bottom)
      })
  }

  private def stdDraw(pos: (Float, Float), rot: Float, off: (Float, Float))(body: => Unit) =
    preserve(withoutTextures {
      translate(pos._1, pos._2)
      rotate(rot)
      translate(off._1, off._2)
      body
    })
}

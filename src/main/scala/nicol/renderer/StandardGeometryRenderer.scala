package nicol
package renderer

import opengl.GLUtils
import geom.{Shape, Quad, AABox, Circle, Line}
import GLUtils._

trait StandardGeometryRenderer {

  implicit object ShapeRenderer extends Renderer[Shape] {
    private val sections = 24
    private val angles = for (a <- 0 until sections) yield a * ((2 * scala.math.Pi.toFloat) / sections)

    import scala.math.{cos, sin}

    def draw(that: Shape, position: (Float, Float), rgb: (Float, Float, Float), rotation: Float, offset: (Float, Float)) = {
      colour(rgb._1, rgb._2, rgb._3)
      that match {
        case Line(start, end) => stdDraw(position, rotation, offset)(GLUtils.draw(Lines) {
          vertex(start.x, start.y)
          vertex(end.x, end.y)
        })
        case Circle(center, radius) => stdDraw(position, rotation, offset)(GLUtils.draw(LineLoop) {
          angles.foreach(a => vertex(center.x + radius * cos(a).toFloat, center.y + radius * sin(a).toFloat))
        })
        case b: AABox => stdDraw(position, rotation, offset)(GLUtils.draw(LineLoop) {
          vertex(b.left, b.top)
          vertex(b.right, b.top)
          vertex(b.right, b.bottom)
          vertex(b.left, b.bottom)
        })
        case Quad(p1, p2, p3, p4) => stdDraw(position, rotation, offset)(GLUtils.draw(LineLoop) {
          vertex(p1.x, p1.y)
          vertex(p2.x, p2.y)
          vertex(p3.x, p3.y)
          vertex(p4.x, p4.y)
        })
      }
    }
  }

  implicit object LineListRenderer extends Renderer[Traversable[Line]] {
    def draw(that: Traversable[Line], position: (Float, Float), rgb: (Float, Float, Float), rotation: Float, offset: (Float, Float)) =
      stdDraw(position, rotation, offset)(GLUtils.draw(Lines) {
        colour(rgb._1, rgb._2, rgb._3)
        that.foreach {
          line =>
            vertex(line.start.x, line.start.y)
            vertex(line.end.x, line.end.y)
        }
      })
  }

  object Filled extends Renderer[Shape] {
    private val sections = 24
    private val angles = for (a <- 0 until sections) yield a * ((2 * scala.math.Pi.toFloat) / sections)

    import scala.math.{cos, sin}

    def draw(that: Shape, position: (Float, Float), rgb: (Float, Float, Float), rotation: Float, offset: (Float, Float)) = {
      colour(rgb._1, rgb._2, rgb._3)
      that match {
        case Line(start, end) => stdDraw(position, rotation, offset)(GLUtils.draw(Lines) {
          vertex(start.x, start.y)
          vertex(end.x, end.y)
        })
        case Circle(center, radius) => stdDraw(position, rotation, offset)(GLUtils.draw(Polygon) {
          angles.foreach(a => vertex(center.x + radius * cos(a).toFloat, center.y + radius * sin(a).toFloat))
        })
        case b: AABox => stdDraw(position, rotation, offset)(GLUtils.draw(Quads) {
          vertex(b.left, b.top)
          vertex(b.right, b.top)
          vertex(b.right, b.bottom)
          vertex(b.left, b.bottom)
        })
        case Quad(p1, p2, p3, p4) => stdDraw(position, rotation, offset)(GLUtils.draw(Quads) {
          vertex(p1.x, p1.y)
          vertex(p2.x, p2.y)
          vertex(p3.x, p3.y)
          vertex(p4.x, p4.y)
        })
      }
    }
  }

  private def stdDraw(pos: (Float, Float), rot: Float, off: (Float, Float))(body: => Unit) =
    preserve(withoutTextures {
      translate(pos._1, pos._2)
      rotate(rot)
      translate(off._1, off._2)
      body
    })
}

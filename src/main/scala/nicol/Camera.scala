package nicol

import opengl.GLUtils
import opengl.GLUtils._
import math._

import org.lwjgl.opengl.Display

trait Camera extends ((=> Unit) => Unit) {
  def bounds: Rect
}

object Pretransformed extends Camera {
  def bounds = Rect(0, 0, Display.getDisplayMode.getWidth, Display.getDisplayMode.getHeight)

  def apply(body: => Unit) = preserve {
    resetTransforms
    body
  }
}

class View(var position: Vector = Vector.zero) extends Camera with Mutable {
  var rotation: Float = 0
  var scale: Float = 1
  var offset: Vector = Vector.zero

  private lazy val len = {
    import scala.math.{sqrt, max}

    val m = max(Display.getDisplayMode.getWidth, Display.getDisplayMode.getHeight)
    sqrt(2 * m * m).toInt
  }

  def bounds = Rect(position.x.toInt, position.y.toInt, len, len)

  def apply(body: => Unit) = preserve {
    resetTransforms
    translate(-position)
    rotate(-rotation)
    GLUtils.scale(this.scale)
    translate(offset)
    body
  }
}
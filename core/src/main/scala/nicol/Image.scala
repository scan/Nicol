package nicol

import math.Rect
import opengl.{GLUtils, Texture}
import GLUtils._

/**
 * Represents a loaded Image. It is very recommended to only create lazy vals of this.
 * Currently, the [[java.awt.Image]] facilities are used to load the image, therefore
 * any format is supported that Java supports: PNG, JPEG, GIF and BMP.
 */
sealed trait Image extends Immutable {
  def width: Int

  def height: Int

  /**
   * Creates an independent sub-image. If the coordinates are outside of the original, they're clipped.
   */
  def sub(r: Rect): Image

  def sub(x: Int, y: Int, w: Int, h: Int): Image = sub(Rect(x, y, w, h))

  def bounds = Rect(0, 0, width, height)

  def draw(position: (Float, Float) = (0, 0), layer: Float = 0, rgb: (Float, Float, Float) = (1, 1, 1), rotation: Float = 0, scale: Float = 1, offset: (Float, Float) = (0, 0))
}

object Image {

  import scala.math.min

  private class GLImage(val texture: Texture) extends Image {
    def width = texture.imageSize._1

    def height = texture.imageSize._2

    private val (w2, h2) = (width / 2, height / 2)

    override def toString = ("<\"" + texture.resource + "\", [" + width + ", " + height + "]>")

    def sub(r: Rect): Image = new GLSubImage(texture, r)

    def draw(p: (Float, Float), layer: Float, rgb: (Float, Float, Float), rotation: Float, s: Float, offset: (Float, Float)) = preserve {
      val (x, y) = p
      texture.bind
      translate(x + w2, y + h2)
      rotate(rotation)
      scale(s)
      translate(offset._1, offset._2)
      GLUtils.draw(Quads) {
        colour(rgb._1, rgb._2, rgb._3)
        texCoord(0, 0)
        vertex(-w2, -h2, layer)
        texCoord(texture.width, 0)
        vertex(w2, -h2, layer)
        texCoord(texture.width, texture.height)
        vertex(w2, h2, layer)
        texCoord(0, texture.height)
        vertex(-w2, h2, layer)
      }
    }
  }

  private class GLSubImage(tex: Texture, rect: Rect) extends GLImage(tex) {
    val tx = (rect.left.toFloat / texture.imageSize._1.toFloat) * texture.width
    val ty = (rect.top.toFloat / texture.imageSize._2.toFloat) * texture.height

    val tw = min((rect.right.toFloat / texture.imageSize._1.toFloat) * texture.width, 1)
    val th = min((rect.bottom.toFloat / texture.imageSize._2.toFloat) * texture.height, 1)

    override def width = rect.width

    override def height = rect.height

    private val (w2, h2) = (width / 2, height / 2)

    override def toString = ("<\"" + texture.resource + "\", [" + width + ", " + height + "], [(" + tx + ", " + ty + "), (" + tw + ", " + th + ")]>")

    override def sub(r: Rect): Image = new GLSubImage(texture, Rect(rect.x + r.x, rect.y + r.y, min(r.width, rect.width), min(r.height, rect.height)))

    override def draw(p: (Float, Float), layer: Float, rgb: (Float, Float, Float), rotation: Float, s: Float, offset: (Float, Float)) = preserve {
      val (x, y) = p
      texture.bind
      translate(x + w2, y + h2)
      rotate(rotation)
      scale(s)
      translate(offset._1, offset._2)
      GLUtils.draw(Quads) {
        colour(rgb._1, rgb._2, rgb._3)
        texCoord(tx, ty)
        vertex(-w2, -h2, layer)
        texCoord(tw, ty)
        vertex(w2, -h2, layer)
        texCoord(tw, th)
        vertex(w2, h2, layer)
        texCoord(tx, th)
        vertex(-w2, h2, layer)
      }
    }
  }

  def apply(res: String): Image = new GLImage(Texture(res))

  def apply(tex: Texture): Image = new GLImage(tex)
}
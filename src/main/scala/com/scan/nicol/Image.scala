package com.scan.nicol

import math.Rect
import opengl.{GLUtils, Texture, Renderer}
import GLUtils._

sealed trait Image extends Immutable {
  def width: Int

  def height: Int

  def sub(r: Rect): Image

  def sub(x: Int, y: Int, w: Int, h: Int): Image = sub(Rect(x, y, w, h))
}

object Image {

  import scala.math.min

  private class GLImage(res: String, val layer: Float) extends Image {
    lazy val texture = Texture(res)

    def width = texture.imageSize._1

    def height = texture.imageSize._1

    override def toString = ("<\"" + res + "\", [" + width + ", " + height + "]>")

    def sub(r: Rect): Image = new GLSubImage(res, layer, r)
  }

  private class GLSubImage(res: String, layer: Float, rect: Rect) extends GLImage(res, layer) {
    lazy val tx = (rect.left.toFloat / texture.imageSize._1.toFloat) * texture.width
    lazy val ty = (rect.top.toFloat / texture.imageSize._2.toFloat) * texture.height

    lazy val tw = min((rect.right.toFloat / texture.imageSize._1.toFloat) * texture.width, 1)
    lazy val th = min((rect.bottom.toFloat / texture.imageSize._2.toFloat) * texture.height, 1)

    override def width = rect.width

    override def height = rect.height

    override def toString = ("<\"" + res + "\", [" + width + ", " + height + "], [(" + tx + ", " + ty + "), (" + tw + ", " + th + ")]>")

    override def sub(r: Rect): Image = new GLSubImage(res, layer, Rect(rect.x + r.x, rect.y + r.y, min(r.width, rect.width), min(r.height, rect.height)))
  }

  def apply(res: String): Image = new GLImage(res, 0)

  def apply(res: String, layer: Float): Image = new GLImage(res, layer)

  def apply(tex: Texture, layer: Float = 0): Image = new GLImage(tex.resource, layer)

  implicit object ImageRenderer extends Renderer[Image] {
    def draw(that: Image, x: Float, y: Float) = that match {
      case img: GLSubImage => {
        val (tx, ty, tw, th) = (img.tx, img.ty, img.tw, img.th)
        img.texture.bind
        GLUtils.draw(Quads) {
          colour(1, 1, 1)
          texCoord(tx, ty)
          vertex(x, y, img.layer)
          texCoord(tw, ty)
          vertex(x + img.width, y, img.layer)
          texCoord(tw, th)
          vertex(x + img.width, y + img.height, img.layer)
          texCoord(tx, th)
          vertex(x, y + img.height, img.layer)
        }
      }
      case img: GLImage => {
        val texture = img.texture
        texture.bind
        GLUtils.draw(Quads) {
          colour(1, 1, 1)
          texCoord(0, 0)
          vertex(x, y, img.layer)
          texCoord(texture.width, 0)
          vertex(x + texture.imageSize._1, y, img.layer)
          texCoord(texture.width, texture.height)
          vertex(x + texture.imageSize._1, y + texture.imageSize._2, img.layer)
          texCoord(0, texture.height)
          vertex(x, y + texture.imageSize._2, img.layer)
        }
      }
    }
  }
}
package com.scan.nicol

import math.Rect
import opengl.{GLUtils, Texture, Renderer}
import GLUtils._

sealed trait Image extends Immutable {
  def width: Int

  def height: Int

  def sub(r: Rect): Image

  def sub(x: Int, y: Int, w: Int, h: Int): Image = sub(Rect(x, y, w, h))

  def draw(x: Float, y: Float)
}

object Image {

  import scala.math.min

  private class GLImage(res: String, val layer: Float) extends Image {
    lazy val texture = Texture(res)

    def width = texture.imageSize._1

    def height = texture.imageSize._1

    override def toString = ("<\"" + res + "\", [" + width + ", " + height + "]>")

    def sub(r: Rect): Image = new GLSubImage(res, layer, r)

    private lazy val list = makeList(GLUtils.draw(Quads) {
      colour(1, 1, 1)
      texCoord(0, 0)
      vertex(0, 0, layer)
      texCoord(texture.width, 0)
      vertex(texture.imageSize._1, 0, layer)
      texCoord(texture.width, texture.height)
      vertex(texture.imageSize._1, texture.imageSize._2, layer)
      texCoord(0, texture.height)
      vertex(0, texture.imageSize._2, layer)
    })


    def draw(x: Float, y: Float) = preserve {
      texture.bind
      translate(x, y)
      list.call
    }
  }

  private class GLSubImage(res: String, layer: Float, rect: Rect) extends GLImage(res, layer) {
    private lazy val list = makeList(GLUtils.draw(Quads) {
      val tx = (rect.left.toFloat / texture.imageSize._1.toFloat) * texture.width
      val ty = (rect.top.toFloat / texture.imageSize._2.toFloat) * texture.height

      val tw = min((rect.right.toFloat / texture.imageSize._1.toFloat) * texture.width, 1)
      val th = min((rect.bottom.toFloat / texture.imageSize._2.toFloat) * texture.height, 1)

      colour(1, 1, 1)
      texCoord(tx, ty)
      vertex(0, 0, layer)
      texCoord(tw, ty)
      vertex(width, 0, layer)
      texCoord(tw, th)
      vertex(width, height, layer)
      texCoord(tx, th)
      vertex(0, height, layer)
    })

    override def width = rect.width

    override def height = rect.height

    override def sub(r: Rect): Image = new GLSubImage(res, layer, Rect(rect.x + r.x, rect.y + r.y, min(r.width, rect.width), min(r.height, rect.height)))

    override def draw(x: Float, y: Float) = preserve {
      texture.bind
      translate(x, y)
      list.call
    }
  }

  def apply(res: String): Image = new GLImage(res, 0)

  def apply(res: String, layer: Float): Image = new GLImage(res, layer)

  def apply(tex: Texture, layer: Float = 0): Image = new GLImage(tex.resource, layer)

  implicit object ImageRenderer extends Renderer[Image] {
    def draw(that: Image, x: Float, y: Float) = that.draw(x, y)
  }

}
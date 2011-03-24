package com.scan.nicol

import math.Rect
import opengl.{GLUtils, Texture}
import GLUtils._

sealed trait Image extends Renderable with Immutable {
  def width: Int

  def height: Int

  def draw(x: Float, y: Float) = draw(x, y, 1, 1, 1)

  def draw(x: Float, y: Float, r: Float, g: Float, b: Float)

  def sub(r: Rect): Image

  def sub(x: Int, y: Int, w: Int, h: Int): Image = sub(Rect(x, y, w, h))
}

object Image {

  import scala.math.min

  private class GLImage(res: String, layer: Float) extends Image {
    lazy val texture = Texture(res)

    def width = texture.imageSize._1

    def height = texture.imageSize._1

    def draw(x: Float, y: Float, r: Float, g: Float, b: Float) = {
      texture.bind
      GLUtils.draw(Quads) {
        colour(r, g, b)
        texCoord(0, 0)
        vertex(x, y, layer)
        texCoord(texture.width, 0)
        vertex(x + texture.imageSize._1, y, layer)
        texCoord(texture.width, texture.height)
        vertex(x + texture.imageSize._1, y + texture.imageSize._2, layer)
        texCoord(0, texture.height)
        vertex(x, y + texture.imageSize._2, layer)
      }
    }

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

    override def draw(x: Float, y: Float, r: Float, g: Float, b: Float) = {
      texture.bind
      GLUtils.draw(Quads) {
        colour(r, g, b)
        texCoord(tx, ty)
        vertex(x, y, layer)
        texCoord(tw, ty)
        vertex(x + width, y, layer)
        texCoord(tw, th)
        vertex(x + width, y + height, layer)
        texCoord(tx, th)
        vertex(x, y + height, layer)
      }
    }

    override def toString = ("<\"" + res + "\", [" + width + ", " + height + "], [(" + tx + ", " + ty + "), (" + tw + ", " + th + ")]>")

    override def sub(r: Rect): Image = new GLSubImage(res, layer, Rect(rect.x + r.x, rect.y + r.y, min(r.width, rect.width), min(r.height, rect.height)))
  }

  def apply(res: String): Image = new GLImage(res, 0)

  def apply(res: String, layer: Float): Image = new GLImage(res, layer)

  def apply(tex: Texture, layer: Float = 0): Image = new GLImage(tex.resource, layer)
}
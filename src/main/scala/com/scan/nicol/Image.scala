package com.scan.nicol

import math.Rect
import opengl.{GLUtils, Texture}
import GLUtils._

sealed trait Image extends Immutable {
  def width: Int

  def height: Int

  def sub(r: Rect): Image

  def sub(x: Int, y: Int, w: Int, h: Int): Image = sub(Rect(x, y, w, h))

  def bounds = Rect(0, 0, width, height)

  def draw(position: (Float, Float) = (0, 0), layer: Float = 0, colour: (Float, Float, Float) = (1, 1, 1), rotation: Float = 0, scale: Float = 1)
}

object Image {

  import scala.math.min

  private class GLImage(res: String) extends Image {
    lazy val texture = Texture(res)

    def width = texture.imageSize._1

    def height = texture.imageSize._2

    private lazy val (w2, h2) = (width / 2, height / 2)

    override def toString = ("<\"" + res + "\", [" + width + ", " + height + "]>")

    def sub(r: Rect): Image = new GLSubImage(res, r)

    def draw(p: (Float, Float), layer: Float, rgb: (Float, Float, Float), rotation: Float, s: Float) = preserve {
      val (x, y) = p
      texture.bind
      translate(x + w2, y + h2)
      rotate(rotation)
      scale(s)
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

  private class GLSubImage(res: String, rect: Rect) extends GLImage(res) {
    lazy val tx = (rect.left.toFloat / texture.imageSize._1.toFloat) * texture.width
    lazy val ty = (rect.top.toFloat / texture.imageSize._2.toFloat) * texture.height

    lazy val tw = min((rect.right.toFloat / texture.imageSize._1.toFloat) * texture.width, 1)
    lazy val th = min((rect.bottom.toFloat / texture.imageSize._2.toFloat) * texture.height, 1)

    override def width = rect.width

    override def height = rect.height

    private lazy val (w2, h2) = (width / 2, height / 2)

    override def toString = ("<\"" + res + "\", [" + width + ", " + height + "], [(" + tx + ", " + ty + "), (" + tw + ", " + th + ")]>")

    override def sub(r: Rect): Image = new GLSubImage(res, Rect(rect.x + r.x, rect.y + r.y, min(r.width, rect.width), min(r.height, rect.height)))

    override def draw(p: (Float, Float), layer: Float, rgb: (Float, Float, Float), rotation: Float, s: Float) = preserve {
      val (x, y) = p
      texture.bind
      translate(x + w2, y + h2)
      rotate(rotation)
      scale(s)
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

  def apply(res: String): Image = new GLImage(res)

  def apply(tex: Texture): Image = new GLImage(tex.resource)
}
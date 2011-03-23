package com.scan.nicol

import opengl.{GLUtils, Texture}
import GLUtils._

class Image(res: String, layer: Float = 0) extends Renderable {
  lazy val texture = Texture(res)

  def width = texture.imageSize._1

  def height = texture.imageSize._1

  def draw(x: Float, y: Float) = draw(x, y, 1, 1, 1)

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
}

object Image {
  def apply(res: String): Image = new Image(res)

  def apply(tex: Texture): Image = new Image(tex.resource)
}
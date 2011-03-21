package com.scan.nicol

import opengl.{GLUtils, Texture}
import GLUtils._

class Image(texture: Texture) extends Renderable {
  def draw(x: Float, y: Float) = {
    texture.bind
    GLUtils.draw(Quads) {
      colour(1, 1, 1)
      texCoord(0, 0)
      vertex(x, y)
      texCoord(texture.width, 0)
      vertex(x + texture.imageSize._1, y)
      texCoord(texture.width, texture.height)
      vertex(x + texture.imageSize._1, y + texture.imageSize._2)
      texCoord(0, texture.height)
      vertex(x, y + texture.imageSize._2)
    }
  }
}

object Image {
  def apply(res: String): Image = new Image(Texture(res))

  def apply(tex: Texture): Image = new Image(tex)
}
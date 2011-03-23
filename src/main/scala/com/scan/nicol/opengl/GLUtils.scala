package com.scan.nicol.opengl

import org.lwjgl.opengl.GL11._

object GLUtils {

  sealed trait PrimitiveMode {
    def glMode: Int
  }

  case object Points extends PrimitiveMode {
    def glMode = GL_POINTS
  }

  case object Lines extends PrimitiveMode {
    def glMode = GL_LINES
  }

  case object Triangles extends PrimitiveMode {
    def glMode = GL_TRIANGLES
  }

  case object Quads extends PrimitiveMode {
    def glMode = GL_QUADS
  }

  def draw(mode: PrimitiveMode)(body: => Unit) = {
    glBegin(mode.glMode)
    body
    glEnd
  }

  def vertex(x: Float, y: Float, z: Float = 0) = glVertex3f(x, y, z)

  def texCoord(t: Float, u: Float) = glTexCoord2f(t, u)

  def colour(r: Float, g: Float, b: Float) = glColor3f(r, g, b)

  def colour(r: Float, g: Float, b: Float, a: Float) = glColor4f(r, g, b, a)
}
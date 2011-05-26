package nicol
package opengl

import org.lwjgl.opengl.GL11._
import math.Vector

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

  case object LineStrip extends PrimitiveMode {
    def glMode = GL_LINE_STRIP
  }

  case object LineLoop extends PrimitiveMode {
    def glMode = GL_LINE_LOOP
  }

  case object Triangles extends PrimitiveMode {
    def glMode = GL_TRIANGLES
  }

  case object Quads extends PrimitiveMode {
    def glMode = GL_QUADS
  }

  case object Polygon extends PrimitiveMode {
    def glMode = GL_POLYGON
  }

  sealed class DrawingList(id: Int) extends Immutable {
    def call = glCallList(id)
  }

  def draw(mode: PrimitiveMode)(body: => Unit) = {
    glBegin(mode.glMode)
    body
    glEnd
  }

  def preserve(body: => Unit) = {
    glPushMatrix
    body
    glPopMatrix
  }

  def translated(v: Vector)(body: => Unit) = preserve {
    glTranslatef(v.x, v.y, 0)
    body
  }

  def translated(x: Float, y: Float)(body: => Unit) = {
    glTranslatef(x, y, 0)
    body
  }

  def rotated(a: Float)(body: => Unit) = {
    glRotatef(a.toDegrees, 0, 0, 1)
    body
  }

  def scaled(x: Float, y: Float)(body: => Unit) = {
    glScalef(x, y, 1)
    body
  }

  def newList(body: => Unit) = {
    val id = glGenLists(1)
    glNewList(id, GL_COMPILE)
    body
    glEndList
    new DrawingList(id)
  }

  def translate(x: Float, y: Float) = glTranslatef(x, y, 0)

  def rotate(a: Float) = glRotatef(a.toDegrees, 0, 0, 1)

  def scale(sx: Float, sy: Float) = glScalef(sx, sy, 1)

  def scale(s: Float) = glScalef(s, s, 1)

  def vertex(x: Float, y: Float, z: Float = 0) = glVertex3f(x, y, z)

  def texCoord(t: Float, u: Float) = glTexCoord2f(t, u)

  def colour(r: Float, g: Float, b: Float) = glColor3f(r, g, b)

  def colour(r: Float, g: Float, b: Float, a: Float) = glColor4f(r, g, b, a)

  def enableTextures = glEnable(GL_TEXTURE_2D)

  def disableTextures = glDisable(GL_TEXTURE_2D)

  def withoutTextures(body: => Unit) {
    glDisable(GL_TEXTURE_2D)
    body
    glEnable(GL_TEXTURE_2D)
  }
}
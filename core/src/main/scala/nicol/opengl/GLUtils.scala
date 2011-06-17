package nicol
package opengl

import org.lwjgl.opengl.GL11._
import math.Vector

/**
 * This module-object is a type-safe. operational safe connection to the
 * OpenGL functions. These are by no means complete, but complete enough
 * for Nicol's 2D areas.
 *
 * Whereever you can, you should not touch these. For drawing primitive
 * Shapes, refer to [[nicol.renderer.GeometryRenderer]], for images, see
 * [[nicol.Image]].
 */
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

  /**
   * Represents a pre-compiled list in VRAM. For further details on
   * this, refer the OpenGL documentation.
   */
  sealed class DrawingList private[GLUtils](id: Int) extends Immutable {
    def call = glCallList(id)
  }

  /**
   * Creates a drawing context for the given primitive mode.
   * @see vertex
   * @see colour
   * @see texCoort
   */
  def draw(mode: PrimitiveMode)(body: => Unit) = {
    glBegin(mode.glMode)
    body
    glEnd
  }

  /**
   * Executes a drawing/transformation operation, using current
   * transforms, but without making changes to them.
   */
  def preserve(body: => Unit) = {
    glPushMatrix
    body
    glPopMatrix
  }

  /**
   * Creates a pre-compiled drawing list.
   */
  def newList(body: => Unit): DrawingList = {
    val id = glGenLists(1)
    glNewList(id, GL_COMPILE)
    body
    glEndList
    new DrawingList(id)
  }

  /**
   * Adds a transation to the transforms.
   */
  def translate(x: Float, y: Float) = glTranslatef(x, y, 0)

  def translate(v: Vector) = glTranslatef(v.x, v.y, 0)

  /**
   * Adds a rotation to the transforms.
   */
  def rotate(a: Float) = glRotatef(a.toDegrees, 0, 0, 1)

  /**
   * Adds 2D scaling to the transforms.
   */
  def scale(sx: Float, sy: Float) = glScalef(sx, sy, 1)

  /**
   * Adds uniform scaling to the transforms.
   */
  def scale(s: Float) = glScalef(s, s, 1)

  /**
   * Draws a 2D vertex.
   */
  def vertex(x: Float, y: Float) = glVertex2f(x, y)

  def vertex(f: (Float, Float)) = glVertex2f(f._1, f._2)

  /**
   * Draws a 3D vertex.
   */
  def vertex(x: Float, y: Float, z: Float) = glVertex3f(x, y, z)

  /**
   * Sets the texture coordinate information for the next
   * vertices. Valid until overridden.
   */
  def texCoord(t: Float, u: Float) = glTexCoord2f(t, u)

  /**
   * Sets the vertex colour for the next vertices. Valid until overridden.
   */
  def colour(r: Float, g: Float, b: Float) = glColor3f(r, g, b)

  /**
   * Sets the vertex colour for the next vertices. Valid until overridden.
   */
  def colour(r: Float, g: Float, b: Float, a: Float) = glColor4f(r, g, b, a)

  /**
   * Removes textures from the enclosed function. All operations
   * for texturing within this context will be ignored.
   */
  def withoutTextures(body: => Unit) {
    glDisable(GL_TEXTURE_2D)
    body
    glEnable(GL_TEXTURE_2D)
  }

  def resetTransforms = glLoadIdentity
}
package nicol
package font

import opengl._

/**
 * Represents a loaded font. Currently, BitmapFonts are not yet supported. For Font loading,
 * the [[java.awt.Font]] system is used.
 */
sealed trait Font extends Immutable {
  /**
   * @note Not the actual pixel height!
   */
  def size: Int

  def name: String

  /**
   * Actual height in pixels
   */
  def height: Int

  /**
   * Writes a string with this font
   */
  def write(str: String, pos: (Float, Float) = (0, 0), rgb: (Float, Float, Float) = (1, 1, 1), rotation: Float = 0)

  /**
   * Gives the width of a string using this font.
   */
  def stringWidth(str: String): Int
}

object Font {

  import org.lwjgl.opengl.GL11._
  import GLUtils._

  /**
   * This the standard rendering font, should be available on every system: Arial, size 12.
   */
  lazy val arial = apply("Arial", 12)

  private class GLFont(val name: String, val size: Int, val height: Int, tex: Int, glyphs: IndexedSeq[GLGlyph]) extends Font {

    def write(str: String, pos: (Float, Float) = (0, 0), rgb: (Float, Float, Float) = (1, 1, 1), rotation: Float = 0) = preserve {
      glBindTexture(GL_TEXTURE_2D, tex)
      translate(pos._1, pos._2)
      rotate(rotation)
      colour(rgb._1, rgb._2, rgb._3)

      str.foreach {
        c =>
          val g = glyphs(c.toInt)
          g.list.call
          translate(g.off, 0)
      }
    }

    def stringWidth(str: String) = str.map(c => glyphs(c.toInt).off).sum
  }

  private case class GLGlyph(list: DrawingList, off: Int)

  def apply(name: String, size: Int): Font = {
    import java.awt._
    import java.awt.image._
    import java.nio._

    val cdist = 1

    def get2fold(t: Int): Int = {
      def tmp(v: Int): Int = {
        if (v < t) tmp(v * 2)
        else v
      }
      tmp(2)
    }

    val font = new java.awt.Font(name, java.awt.Font.BOLD, size)
    var img = new BufferedImage(1, 1, BufferedImage.TYPE_4BYTE_ABGR)
    var g = img.getGraphics.asInstanceOf[Graphics2D]
    g.setFont(font)

    val fm = g.getFontMetrics
    val width = (for (c <- 0 until 256) yield (fm.charWidth(c.toChar))).sum
    val height = fm.getHeight

    val (rw, rh) = (get2fold(width + cdist), get2fold(fm.getHeight))

    g.dispose
    img = new BufferedImage(rw, rh, BufferedImage.TYPE_4BYTE_ABGR)

    g = img.getGraphics.asInstanceOf[Graphics2D]
    g.setFont(font)

    g.setColor(new Color(0xFFFFFFFF, true));
    g.setBackground(new Color(0x00000000, true));

    var w = 0
    val glyphs = for (i <- 0 until 256) yield {
      g.drawString(i.toChar.toString, w + cdist, fm.getAscent)

      val foff = ((w.toFloat + cdist) / rw.toFloat)
      val off = fm.charWidth(i.toChar)
      val (tw, th) = ((off.toFloat / rw.toFloat), (height.toFloat / rh.toFloat))

      val list = newList(draw(Quads) {
        texCoord(foff, 0)
        vertex(0, 0)

        texCoord(foff + tw, 0)
        vertex(off, 0)

        texCoord(foff + tw, th)
        vertex(off, height)

        texCoord(foff, th)
        vertex(0, height)
      })

      w += off
      GLGlyph(list, off)
    }

    val data = img.getRaster.getDataBuffer.asInstanceOf[DataBufferByte].getData

    val buf = ByteBuffer.allocateDirect(data.length)
    buf.order(ByteOrder.nativeOrder)
    buf.put(data, 0, data.length)
    buf.flip

    g.dispose

    import org.lwjgl.opengl.GL11._

    val tex = glGenTextures
    glBindTexture(GL_TEXTURE_2D, tex)

    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR)
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR)
    glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, img.getWidth, img.getHeight, 0, GL_RGBA, GL_UNSIGNED_BYTE, buf)

    new GLFont(name, size, height, tex, glyphs.toIndexedSeq)
  }
}
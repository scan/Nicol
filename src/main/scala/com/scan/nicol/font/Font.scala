package com.scan.nicol
package font

import opengl._
import scala.Float._

trait Font {
  def size: Int

  def name: String

  def write(str: String, pos: (Float, Float) = (0, 0), rgb: (Float, Float, Float) = (1, 1, 1))
}

object Font {

  import org.lwjgl.opengl.GL11._
  import GLUtils._

  lazy val arial = apply("Arial", 15)

  private class GLFont(val name: String, val size: Int, tex: Int, glyphs: IndexedSeq[GLGlyph]) extends Font {

    def write(str: String, pos: (Float, Float) = (0, 0), rgb: (Float, Float, Float) = (1, 1, 1)) = {
      glBindTexture(GL_TEXTURE_2D, tex)
      var m = 0
      str.foreach {
        c =>
          val g = glyphs(c.toInt)
          preserve(translated(pos._1 + m, pos._2) {
            colour(rgb._1, rgb._2, rgb._3)
            g.list.call
          })
          m += g.off
      }
    }

  }

  private case class GLGlyph(list: DrawingList, off: Int)

  def apply(name: String, size: Int): Font = {
    import java.awt._
    import java.awt.image._
    import java.nio._

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

    val (rw, rh) = (get2fold(width), get2fold(fm.getHeight))

    g.dispose
    img = new BufferedImage(rw, rh, BufferedImage.TYPE_4BYTE_ABGR)

    g = img.getGraphics.asInstanceOf[Graphics2D]
    g.setFont(font)

    g.setColor(new Color(0xFFFFFFFF, true));
    g.setBackground(new Color(0x00000000, true));

    var w = 0
    val glyphs = for (i <- 0 until 256) yield {
      g.drawString(i.toChar.toString, w + 1, fm.getAscent)

      val foff = (w.toFloat / rw.toFloat)
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

      val glyph = GLGlyph(list, off)
      w += off
      glyph
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
    glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, img.getWidth(), img.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, buf)

    new GLFont(name, size, tex, glyphs.toIndexedSeq)
  }
}
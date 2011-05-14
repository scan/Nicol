package com.scan.nicol
package font

import opengl._
import GLUtils._

trait Font {
  def size: Int

  def name: String

  def write(str: String, pos: (Float, Float) = (0, 0), rgb: (Float, Float, Float) = (1, 1, 1))
}

object Font {

  lazy val arial = apply("Arial", 10)

  private class GLFont(val name: String, val size: Int, lists: IndexedSeq[DrawingList]) extends Font {
    private val d = 1f / 16f

    def write(str: String, pos: (Float, Float) = (0, 0), rgb: (Float, Float, Float) = (1, 1, 1)) = preserve(
      for (x <- 0 until str.length) {
        translated(pos._1 + (x * d), pos._1)(lists(x).call)
      })
  }

  def apply(name: String, size: Int): Font = {
    import java.awt._
    import java.awt.geom._
    import java.awt.image._
    import java.nio._

    val font = new java.awt.Font(name, java.awt.Font.BOLD, size)
    val img = new BufferedImage(512, 512, BufferedImage.TYPE_4BYTE_ABGR)
    val g = img.getGraphics.asInstanceOf[Graphics2D]
    g.setFont(font)

    val fm = g.getFontMetrics
    val (width, height) = (fm.stringWidth("W"), fm.getHeight)
    val lineWidth = if (width > height) width * 16 else height * 16

    g.setFont(font);
    g.setColor(new Color(0xFFFFFFFF, true));
    g.setBackground(new Color(0x00000000, true));

    for (i <- 0 until 256) {
      val (x, y) = (i % 16, i / 16)
      g.drawString(i.toChar.toString, (x * 32) + 1, (y * 32) + fm.getAscent)
    }

    val buf = ByteBuffer.allocateDirect(4 * img.getWidth * img.getHeight)
    val data = img.getRaster.getDataElements(0, 0, img.getWidth, img.getHeight, null).asInstanceOf[Array[Byte]]
    buf.clear
    buf.put(data)
    buf.rewind

    import org.lwjgl.opengl.GL11._
    import GLUtils._

    val tex = glGenTextures
    glBindTexture(GL_TEXTURE_2D, tex)

    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR)
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR)
    glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, img.getWidth(), img.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, buf)

    val base = glGenLists(256)

    val d = 1f / 16f

    val lists = for (i <- 0 until 256) yield {
      val (u, v) = ((i % 16).toFloat / 16f, 1f - ((i / 16).toFloat / 16f))
      newList(base + i) {
        glBindTexture(GL_TEXTURE_2D, tex)
        draw(Quads) {
          texCoord(u, v)
          vertex(-0.0450f, 0.0450f)
          texCoord(u + d, v)
          vertex(0.0450f, 0.0450f)
          texCoord(u + d, v - d)
          vertex(0.0450f, -0.0450f)
          texCoord(u, v - d)
          vertex(-0.0450f, -0.0450f)
        }
      }
    }

    new GLFont(name, size, lists)
  }
}
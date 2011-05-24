package com.scan.nicol
package font

import opengl._

trait Font {
  def size: Int

  def name: String

  def write(str: String, pos: (Float, Float) = (0, 0), rgb: (Float, Float, Float) = (1, 1, 1))
}

object Font {

  import org.lwjgl.opengl.GL11._
  import GLUtils._

  lazy val arial = apply("Arial", 15)

  private class GLFont(val name: String, val size: Int, tex: Int, woff: Float, hoff: Float) extends Font {

    def write(str: String, pos: (Float, Float) = (0, 0), rgb: (Float, Float, Float) = (1, 1, 1)) = {
      glBindTexture(GL_TEXTURE_2D, tex)
      var m = 0
      str.foreach {
        c =>
          val i = c.toInt
          val (u, v) = ((i % 16) * woff, (i / 16) * hoff)
          draw(Quads) {
            colour(rgb._1, rgb._2, rgb._3)
            texCoord(u, v)
            vertex(pos._1 + m, pos._2)
            texCoord(u + woff, v)
            vertex(pos._1 + m + size, pos._2)
            texCoord(u + woff, v + hoff)
            vertex(pos._1 + m + size, pos._2 + size)
            texCoord(u, v + hoff)
            vertex(pos._1 + m, pos._2 + size)
          }
          m += size
      }
    }

  }

  def apply(name: String, size: Int): Font = {
    import java.awt._
    import java.awt.geom._
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
    val (width, height) = (fm.stringWidth("W"), fm.getHeight)
    val (rw, rh) = (get2fold(width * 16 + 1), get2fold(height * 16 + fm.getAscent))

    g.dispose
    img = new BufferedImage(rw, rh, BufferedImage.TYPE_4BYTE_ABGR)

    g = img.getGraphics.asInstanceOf[Graphics2D]
    g.setFont(font)

    g.setColor(new Color(0xFFFFFFFF, true));
    g.setBackground(new Color(0x00000000, true));

    for (i <- 0 until 256) {
      val (x, y) = (i % 16, i / 16)
      g.drawString(i.toChar.toString, (x * width) + 1, (y * height) + fm.getAscent)
    }

    import javax.imageio.ImageIO.write
    write(img, "png", new java.io.File(name + size + ".png"))

    println(rw.toString + "x" + rh.toString)
    println(width.toString + "x" + height.toString)

    val data = img.getRaster.getDataBuffer.asInstanceOf[DataBufferByte].getData

    val buf = ByteBuffer.allocateDirect(data.length)
    buf.order(ByteOrder.nativeOrder)
    buf.put(data, 0, data.length)
    buf.flip

    import org.lwjgl.opengl.GL11._

    val tex = glGenTextures
    glBindTexture(GL_TEXTURE_2D, tex)

    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR)
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR)
    glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, img.getWidth(), img.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, buf)

    new GLFont(name, size, tex,
      (width.toFloat / rw.toFloat), (height.toFloat / rh.toFloat))
  }
}
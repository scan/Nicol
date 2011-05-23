package com.scan.nicol.opengl

import org.lwjgl.opengl._
import GL11._

sealed abstract class Texture private(val resource: String) extends Immutable {
  def width: Float

  def height: Float

  /**
   * Size as a ratio between the actual size and the size of the texture data.
   */
  def size: (Float, Float)

  /**
   * The actual size of the image
   */
  def imageSize: (Int, Int)

  /**
   * Binds this texture for usage. Usually, this does not have to be called by the user.
   */
  def bind: Unit
}

object Texture {

  private class GLTexture(res: String, id: Int, imgSize: (Int, Int), texSize: (Int, Int)) extends Texture(res) {
    lazy val width = imgSize._1.toFloat / texSize._1.toFloat

    lazy val height = imgSize._2.toFloat / texSize._2.toFloat

    def size = (width, height)

    def imageSize = imgSize

    def bind = glBindTexture(GL_TEXTURE_2D, id)
  }


  def apply(res: String): Texture = getTexture(res)

  def apply(xs: String*): Seq[Texture] = xs.map(apply _)

  private def getTexture(res: String, srcPixel: Int = GL_RGBA, minFilter: Int = GL_LINEAR, magFilter: Int = GL_LINEAR): Texture = {
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

    def convertImageData(img: BufferedImage) = {
      val texSize = (get2fold(img.getWidth), get2fold(img.getHeight))

      import java.awt.color.ColorSpace._
      val glAlphaColorModel = new ComponentColorModel(getInstance(CS_sRGB), Array(8, 8, 8, 8), true, false, Transparency.TRANSLUCENT, DataBuffer.TYPE_BYTE)
      val glColorModel = new ComponentColorModel(getInstance(CS_sRGB), Array(8, 8, 8, 0), false, false, Transparency.OPAQUE, DataBuffer.TYPE_BYTE)

      val img2 = if (img.getColorModel.hasAlpha) {
        val raster = Raster.createInterleavedRaster(DataBuffer.TYPE_BYTE, texSize._1, texSize._2, 4, null)
        new BufferedImage(glAlphaColorModel, raster, false, null)
      } else {
        val raster = Raster.createInterleavedRaster(DataBuffer.TYPE_BYTE, texSize._1, texSize._2, 3, null)
        new BufferedImage(glColorModel, raster, false, null)
      }

      val g = img2.getGraphics
      g.setColor(new Color(0, 0, 0, 0))
      g.fillRect(0, 0, texSize._1, texSize._2)
      g.drawImage(img, 0, 0, null)

      val data = img2.getRaster.getDataBuffer.asInstanceOf[DataBufferByte].getData

      val buf = ByteBuffer.allocateDirect(data.length)
      buf.order(ByteOrder.nativeOrder)
      buf.put(data, 0, data.length)
      buf.flip

      (buf, texSize)
    }

    @throws(classOf[java.io.FileNotFoundException])
    def loadImage = {
      val img = new javax.swing.ImageIcon(res).getImage
      if (img == null)
        throw new java.io.FileNotFoundException(res)
      else {
        val buf = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB)
        val g = buf.getGraphics
        g.drawImage(img, 0, 0, null)
        g.dispose

        buf
      }
    }

    val id = glGenTextures
    glBindTexture(GL_TEXTURE_2D, id)

    val img = loadImage

    val pixel = if (img.getColorModel.hasAlpha) GL_RGBA else GL_RGB

    val (buf, texSize) = convertImageData(img)

    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, minFilter)
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, magFilter)

    glTexImage2D(GL_TEXTURE_2D, 0, pixel, get2fold(img.getWidth), get2fold(img.getHeight), 0, srcPixel, GL_UNSIGNED_BYTE, buf)

    new GLTexture(res, id, (img.getWidth, img.getHeight), texSize)
  }

}
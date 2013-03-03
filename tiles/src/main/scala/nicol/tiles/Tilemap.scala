package nicol
package tiles

class FormatNotSupportException(str: String) extends Exception(str)

class Tilemap(ls: Array[Layer]) extends Immutable with DrawableEntity {
  val layers = ls.sorted

  def update = layers.foreach(_.update)

  def draw(x: Float, y: Float) = layers.foreach(_.draw(x, y))

  def apply(x: Int)(y: Int) = for (l <- layers) yield l match {
    case l: TileLayer => l.tiles(y)(x)
  }
}

import scala.xml._


/* TODO: Implement working loading function */
object Tilemap {
  def apply(res: String): Tilemap = {
    throw new Exception("Not implemented yet")

    val url = classOf[Tilemap].getClassLoader.getResource(res)
    val root = XML.load(url)

    if ((root \ "@version").text != "1.0") throw new FormatNotSupportException("Not TMX V0.1")

    val width = (root \ "@width").text.toInt
    val height = (root \ "@height").text.toInt

    val tilesets = {
      val tw = (root \ "@tilewidth").text.toInt
      val th = (root \ "@tileheight").text.toInt

      for (node <- (root \ "tileset")) yield {
        if ((root \ "@tilewidth").text != (node \ "@tilewidth").text || (root \ "@tileheight").text != (node \ "@tileheight").text) throw new FormatNotSupportException("Root tilesize and tileset size must be equal")

        val toffset = if (node.exists(_.label == "tileoffset")) {
          val tnode = (node \ "tileoffset").head
          ((tnode \ "@x").text.toInt, (tnode \ "@y").text.toInt)
        } else (0, 0)

        Tileset((node \ "@source").text, (tw, th), toffset)
      }
    }
    val tset = tilesets.head

    var level = 0
    val layers = for (node <- (root \ "layer")) yield {
      level += 1
      val data = (node \ "data").head
      val inflater = (data.attribute("compression")) match {
        case None => IdentityInflater
        case Some(attr) => attr.head.label match {
          case "gzip" => GzipInflater
          case x => throw new NotSupportedException("Compression method '" + x + "' not supported")
        }
      }

      val importer = (data.attribute("encoding")) match {
        case None => throw new NotImplementedException("Default encoding not yet implemented")
        case Some(attr) => attr.head.label match {
          case "base" => new Base64Importer(inflater, width)
          case "csv" => new CSVImporter(inflater)
          case x => throw new NotSupportedException("Compression method '" + x + "' not supported")
        }
      }

      val tiles = importer(data.text).map(_.map(n => tset(n)))
      new TileLayer(tiles, tset.tileSize, level.toFloat)
    }

    new Tilemap(layers.toArray)
  }

  private def byteToIntArray(bytes: Seq[Byte]): Seq[Int] = {
    import java.nio.{ByteBuffer, ByteOrder}

    val intBuf = ByteBuffer.wrap(bytes.toArray).order(ByteOrder.BIG_ENDIAN).asIntBuffer()
    val array = Array.ofDim[Int](intBuf.remaining())
    intBuf.get(array)
    array.toSeq
  }

  private abstract class Importer(inflater: Inflater) extends (String => Array[Array[Int]]) {
    val FLIPPED_HORIZONTALLY_FLAG = 0x80000000
    val FLIPPED_VERTICALLY_FLAG = 0x40000000
    val FLIPPED_DIAGONALLY_FLAG = 0x20000000
  }

  private class Base64Importer(inflater: Inflater, pitch: Int) extends Importer(inflater) {
    def apply(s: String): Array[Array[Int]] = byteToIntArray(inflater(nicol.util.base64_decode(s))).map(_ & ~(FLIPPED_DIAGONALLY_FLAG | FLIPPED_HORIZONTALLY_FLAG | FLIPPED_DIAGONALLY_FLAG)).grouped(pitch).map(_.toArray).toArray
  }

  private class CSVImporter(inflater: Inflater) extends Importer(inflater) {
    def apply(s: String): Array[Array[Int]] = throw new NotImplementedException("To be implemented: CSV importer")
  }

  private trait Inflater extends (Seq[Byte] => Seq[Byte])

  private object IdentityInflater extends Inflater {
    @inline
    def apply(s: Seq[Byte]) = s
  }

  private object GzipInflater extends Inflater {
    def apply(s: Seq[Byte]) = nicol.util.decompress(s)
  }

}
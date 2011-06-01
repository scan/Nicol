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

    val tileset = {
      val tw = (root \ "@tilewidth").text.toInt
      val th = (root \ "@tileheight").text.toInt

      if ((root \ "tileset").length != 1) throw new FormatNotSupportException("Only one tileset per map supported")
      val node = (root \ "tileset")(0)
      if ((root \ "@tilewidth").text != (node \ "@tilewidth").text || (root \ "@tileheight").text != (node \ "@tileheight").text) throw new FormatNotSupportException("Root tilesize and tileset size must be equal")

      Tileset((node \ "@source").text, (tw, th))
    }

    val layers = for(node <- (root \ "layer")) yield {
      val data = node \ "data"
      (data \ "@compression").text match {
        case "gzip" => {}
        case _ => throw new FormatNotSupportException("Compression method not supported");
      }
    }

    null
  }
}
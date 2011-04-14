package com.scan.nicol
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

object Tilemap {
  def apply(res: String): Tilemap = {
    val url = classOf[Tilemap].getClassLoader.getResource(res)
    val xml = XML.load(url)

    if(((xml \ "map")(0) \ "@version").text != "1.0") throw new FormatNotSupportException("Not TMX V0.1")

    null
  }
}
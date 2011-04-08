package com.scan.nicol
package tiles

class Tilemap(ls: Array[Layer]) extends Immutable with DrawableEntity {
  val layers = ls.sorted

  def update = layers.foreach(_.update)

  def draw(x: Float, y: Float) = layers.foreach(_.draw(x, y))

  def apply(x: Int)(y: Int) = for (l <- layers) yield l match {
    case l: TileLayer => l.tiles(y)(x)
  }
}

object Tilemap {

}
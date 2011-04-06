package com.scan.nicol
package tiles

abstract class Layer(level: Float)

class TileLayer[A](tset: Tileset, tiles: Array[Array[A]], level: Float = 0) extends Layer(level)

class EntityLayer(entities: Array[Entity], level: Float = 0) extends Layer(level) with Entity {
  def update = entities.foreach(_.update)
}
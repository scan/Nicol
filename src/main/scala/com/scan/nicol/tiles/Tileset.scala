package com.scan.nicol
package tiles

import math.Rect

case class Tile(area: Rect, walkable: Boolean = true)

sealed class Tileset(res: String, tsize: (Int, Int)) {
  lazy val img = Image(res)

  lazy val tiles = {
    for (y <- 0 to num_y;
         x <- 0 to num_x) yield Tile(Rect(x * tsize._1, y * tsize._2, tsize._1, tsize._2))
  }.toArray

  private[nicol] lazy val (num_x, num_y) = (img.width / tsize._1 - 1, img.height / tsize._2 - 1)

  def tileSize = tsize

  def tileWidth = tsize._1

  def tileHeight = tsize._2

  def apply(n: Int) = img.sub(tiles(n).area)

  def apply(t: Tile) = img.sub(t.area)

  def length = tiles.length

  def foreach(body: Image => Unit) = tiles foreach (t => body(img sub t.area))
}

object Tileset {
  def apply(res: String, tsize: (Int, Int)): Tileset = new Tileset(res, tsize)
}
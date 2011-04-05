package com.scan.nicol
package tiles

import math.Rect

case class Tile(area: Rect, walkable: Boolean = true)

sealed class Tileset(res: String, tsize: (Int, Int)) {
  lazy val img = Image(res)

  lazy val tiles = {
    val (num_x, num_y) = (img.width / tsize._1, img.height / tsize._2)

    for (y <- 0 to num_y - 1;
         x <- 0 to num_x - 1) yield Tile(Rect(x * tsize._1, y * tsize._2, tsize._1, tsize._2))
  }.toArray

  def apply(n: Int) = img.sub(tiles(n).area)

  def length = tiles.length

  def foreach(body: (Image) => Unit) = for (n <- 0 to length - 1) body(img sub (tiles(n).area))
}

object Tileset {
  def apply(res: String, tsize: (Int, Int)): Tileset = new Tileset(res, tsize)
}
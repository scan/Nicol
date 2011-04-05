package com.scan.nicol
package tiles

import math.Rect

case class Tile(area: Rect, walkable: Boolean = true)

sealed class Tileset(img: Image, tiles: Array[Tile]) {
  def apply(n: Int) = img.sub(tiles(n).area)

  def length = tiles.length
}

object Tileset {
  def apply(res: String, tsize: (Int, Int)): Tileset = makeTileset(Image(res), tsize)

  def apply(img: Image, tsize: (Int, Int)): Tileset = makeTileset(img, tsize)

  private def makeTileset(img: Image, tsize: (Int, Int)) = {
    val (num_x, num_y) = (img.width / tsize._1, img.height / tsize._2)

    val arr = for (y <- 0 to num_y-1;
                   x <- 0 to num_x-1) yield Tile(Rect(x * tsize._1, y * tsize._2, tsize._1, tsize._2))
    new Tileset(img, arr.toArray)
  }
}
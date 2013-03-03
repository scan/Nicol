package nicol
package tiles

import math.Rect
import nicol.{Image}

trait Tile {
  def image: Image
}

sealed class Tileset private (res: String, tsize: (Int, Int), toffest: (Int, Int) = (0,0)) {
  lazy val img = Image(res)

  lazy val tiles = {
    for (y <- 0 to num_y;
         x <- 0 to num_x) yield new Tile { val image = img.sub(Rect(x * tsize._1, y * tsize._2, tsize._1, tsize._2)) }
  }.toArray

  private[nicol] lazy val (num_x, num_y) = (img.width / tsize._1 - 1, img.height / tsize._2 - 1)

  def tileSize = tsize

  def tileWidth = tsize._1

  def tileHeight = tsize._2

  def apply(n: Int) = tiles(n)

  //def apply(t: Tile) = img.sub(t.area)

  def apply(row: Int, col: Int) = tiles(row * num_x + col)

  def length = tiles.length

  def foreach(body: Image => Unit) = tiles foreach (t => body(t.image))
}

object Tileset {
  def apply(res: String, tsize: (Int, Int), toffset: (Int, Int) = (0,0)): Tileset = new Tileset(res, tsize, toffset)
}
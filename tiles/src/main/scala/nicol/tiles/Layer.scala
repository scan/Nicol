package nicol
package tiles

abstract class Layer(val level: Float) extends DrawableEntity

object Layer {

  implicit object LayerSort extends Ordering[Layer] {
    def compare(x: Layer, y: Layer) = Ordering.Float.compare(x.level, y.level)
  }

}

class TileLayer(val tiles: Array[Array[Tile]], tsize: (Int, Int), level: Float = 0) extends Layer(level) {
  def draw(sx: Float, sy: Float) = for (y <- 0 to tiles.length - 1;
                                        x <- 0 to tiles(0).length - 1) {
    tiles(y)(x).image.draw((x * tsize._1 - sx, y * tsize._2 - sy))
  }

  def update = {}

  def apply(y: Int, x: Int) = tiles(y)(x)
}

class EntityLayer(entities: Array[Entity], level: Float = 0) extends Layer(level) {
  def update = entities.foreach(_.update)

  def draw(x: Float, y: Float) = entities.foreach(e => e match {
    case e: DrawableEntity => e.draw(x, y)
  })
}
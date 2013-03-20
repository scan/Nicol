package nicol
package tiles
package tmx

trait TiledMap {

  import TiledMap._

  sealed trait MapOrientation

  case object Orthogonal extends MapOrientation

  case object Isometric extends MapOrientation

  def path: String

  def orientation: MapOrientation

  def width: Int

  def height: Int

  def tileWidth: Int

  def tileHeight: Int

  def properties: Properties = Map.empty

  def tilesets: Seq[Tileset]

  def layers: Seq[Layer]
}

object TiledMap {

  type Properties = Map[String, String]

  private[nicol] val FLIPPED_HORIZONTALLY_FLAG = 0x80000000
  private[nicol] val FLIPPED_VERTICALLY_FLAG = 0x40000000
  private[nicol] val FLIPPED_DIAGONALLY_FLAG = 0x20000000

  /* TODO: Implement loader */
}

sealed trait Tileset {
  def name: String

  private[nicol] def initialGid: Int

  def tileWidth: Int

  def tileHeight: Int

  def images: Seq[Image]

  def properties: Map[Int, TiledMap.Properties] = Map.empty
}

sealed trait TileObject {

  import TileObject._

  def name: String = ""

  def objType: String = ""

  def properties: TiledMap.Properties = Map.empty

  def x: Int

  def y: Int

  def width: Int

  def height: Int

  def gid: Int = 0

  def polygon: Polygon = null

  def polyline: Polyline = null
}

object TileObject {
  type Polygon = Seq[(Int, Int)]
  type Polyline = Seq[(Int, Int)]
}

sealed trait Layer {
  def name: String
}
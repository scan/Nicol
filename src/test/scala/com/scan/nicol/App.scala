package com.scan.nicol

import tiles._

object App extends Game("Nicol example App", 800, 600) {

  import scala.util.Random._

  val image = Image("sika.png")

  val tileset = Tileset("sometiles.png", (16, 16))

  override def init = {
    println("Number of tiles: " + tileset.length + ": " + (tileset.num_x, tileset.num_y))
    println("Tileimage: " + tileset.img)
    println("SikaImage: " + image)

    //tileset.tiles.foreach(t => println(t.area.toString))
  }

  def update = {
    for (n <- 0 to tileset.length - 1) tileset(n).draw(n * tileset.tileWidth, 0)

    sync(60)
  }
}
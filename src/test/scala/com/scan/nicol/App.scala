package com.scan.nicol

import tiles._

object App extends Game("Nicol example App", 800, 600) {

  import scala.util.Random._

  val image = Image("sika.png")

  val tileset = Tileset("sometiles.png", (64, 64))

  def update = {
    for (n <- 0 to tileset.length - 1) tileset(n).draw(n * tileset.tileWidth, 0)

    draw(image, (400 - image.width / 2, 300 - image.height / 2))

    sync(60)
  }
}
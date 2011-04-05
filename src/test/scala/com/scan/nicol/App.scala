package com.scan.nicol

import tiles._

object App extends Game("Nicol example App", 800, 600) {

  import scala.util.Random._

  val image = Image("sika.png") sub (10, 10, 32, 32)

  val tileset = Tileset("sometiles.png", (64, 64))

  def update = {
    for (n <- 0 to tileset.length - 1) tileset(n).draw(0, n * 64)

    sync(60)
  }
}
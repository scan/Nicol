package com.scan.nicol

import tiles._
import input._

object App extends Game("Nicol example App", 800, 600) {

  import scala.util.Random._

  val image = Image("sika.png")

  val tileset = Tileset("sometiles.png", (64, 64))

  var (x, y) = (400, 300)

  def update = {
    for (n <- 0 to tileset.length - 1) tileset(n).draw(n * tileset.tileWidth, 0)

    draw(image, (x - image.width / 2, y - image.height / 2))

    import Key._
    if (Left) x -= 5
    if (Right) x += 5
    if (Up) y -= 5
    if (Down) y += 5

    sync(60)
  }
}
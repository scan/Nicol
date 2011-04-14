package com.scan.nicol

import tiles._
import input.Key._
import geom._
import math._

object App extends Game("Nicol example App", 800, 600) {

  import scala.util.Random._

  val image = Image("sika.png")

  val tileset = Tileset("sometiles.png", (64, 64))

  var (x, y) = (400, 300)
  var a = 0f

  def update = {
    for (n <- 0 to tileset.length - 1) tileset(n).draw((n * tileset.tileWidth, 0))

    image.draw(position = (x - image.width / 2, y - image.height / 2), rotation = a)

    if (left) a -= 0.1f
    if (right) a += 0.1f

    draw(Circle((x, y), 50), rgb = (1, 0, 0))

    sync(60)
  }
}
package com.scan.nicol

import tiles._
import input.Key._
import geom._
import math._
import font._

object App extends Game("Nicol example App", 800, 600) {
  game =>

  import scala.util.Random._
  import scala.math.{sin, cos}

  val image = Image("sika.png")

  val tileset = Tileset("sometiles.png", (64, 64))

  val gameRect = Rect(0, 0, 800, 600)

  var (x, y) = (400, 300)
  var a = 0f

  var bullets = collection.mutable.ListBuffer[Bullet]()

  def update = {
    // for (n <- 0 to tileset.length - 1) tileset(n).draw((n * tileset.tileWidth, 0))
    if (left) a -= 0.1f
    if (right) a += 0.1f
    if (space) bullets += new Bullet(a)

    bullets = bullets.filter(!_.finished)

    bullets.foreach {
      b =>
        b.update
        b.draw
    }

    val r = 50
    val redCircle = Circle((x, y), r)

    val targetCircle = Circle((
      x + (r * cos(a)).toFloat,
      y + (r * sin(a)).toFloat
      ), radius = r / 5
    )

    draw(redCircle, rgb = (1, 0, 0))
    draw(targetCircle, rgb = (0, 1, 0))
    image.draw(position = (x - image.width / 2, y - image.height / 2), rotation = a)
    draw("Hello, Nicol!", position = (30, 30), rgb = (1, 1, 1))(font.Font.StringRenderer);

    sync(60)
  }

  class Bullet(d: Float) extends Entity {
    var (x, y) = (game.x, game.y)
    var finished = false
    val speed = 10

    def update {
      x += (speed * cos(d)).toInt
      y += (speed * sin(d)).toInt
      finished = !gameRect.collides((x, y))
    }

    def draw {
      game.draw(Circle((x, y), 3), rgb = (250, 250, 210))
    }
  }

}

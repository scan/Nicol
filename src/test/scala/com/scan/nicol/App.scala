package com.scan.nicol

import input.Key._
import geom._
import math._

object App extends Game(EntryScene("Nicol example App", 800, 600)(Main))

object Main extends GameScene {
  scene =>

  import scala.math.{sin, cos}

  val image = Image("sika.png")

  val gameRect = Rect(0, 0, 800, 600)

  var (x, y) = (400, 300)
  var a = 0f

  var bullets = collection.mutable.ListBuffer[Bullet]()

  def update: Scene = {
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

    val r = 50f
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

    if (escape) return End else this
  }

  class Bullet(d: Float) extends Entity {
    var (bx, by) = (x, y)
    var finished = false
    val speed = 10

    def update {
      bx += (speed * cos(d)).toInt
      by += (speed * sin(d)).toInt
      finished = !gameRect.collides((bx, by))
    }

    def draw {
      scene.draw(Circle((bx, by), 3), rgb = (250, 250, 210))
    }
  }

}
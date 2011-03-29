package com.scan.nicol

object App extends Game("Nicol example App", 800, 600) {

  import scala.util.Random._

  val image = Image("sika.png") sub (10, 10, 32, 32)

  val positions = Array.tabulate[(Float, Float)](800 / 32, 600 / 32)((x, y) => (x * 32, y * 32))

  def update = {
    positions.foreach(_.foreach(p => {
      draw(image, p._1, p._2)
    }))

    sync(60)
  }
}
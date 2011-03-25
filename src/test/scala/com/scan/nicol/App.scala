package com.scan.nicol

object App extends Game("Nicol example App", 800, 600) {

  import scala.util.Random._

  val image = Image("sika.png")

  val positions: Seq[(Float, Float)] = Seq.fill(1000)((nextInt(800), nextInt(600)))

  def update = {
    for (p <- positions) {
      draw(image, p._1, p._2)
    }

    //sync(60)
  }
}
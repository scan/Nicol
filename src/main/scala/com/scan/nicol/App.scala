package com.scan.nicol

object App extends Game("Nicol example App", 800, 600) {
  val image = Image("sika.png")

  def update = {
    image.draw(0, 0)
  }
}
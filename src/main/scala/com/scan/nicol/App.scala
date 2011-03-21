package com.scan.nicol

object App extends Game {
  var image: Image = null

  override def init = {
    image = Image("sika.png")
  }

  def update = {
    image.draw(0, 0)
  }
}
package com.scan.nicol

object App extends Game("Nicol example App", 800, 600) {

  val image = Image("sika.png")

  def update = {
    draw(image)

    //sync(60)
  }
}
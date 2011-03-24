package com.scan.nicol

object App extends Game("Nicol example App", 800, 600) {
  val image = Image("sika.png") sub (13, 14, 20, 20)

  def update = {
    (image).draw(0, 0)

    sync(60)
  }
}
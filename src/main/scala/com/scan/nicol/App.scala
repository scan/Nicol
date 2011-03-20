package com.scan.nicol

import opengl._
import org.lwjgl.opengl._

object App extends Application {

  import Display._

  setDisplayMode(new DisplayMode(800, 600))
  create

  println(Texture("sika.png").toString)

  while (!isCloseRequested) {
    update
  }

  destroy
}
package com.scan.nicol

import org.lwjgl.opengl._
import scala.actors._

trait Game extends Application with Actor {

  val width = 800
  val height = 600

  start

  def act = {
    import Display._

    setDisplayMode(new DisplayMode(width, height))
    create

    import GL11._

    glEnable(GL_TEXTURE_2D)

    glMatrixMode(GL_PROJECTION)
    glLoadIdentity
    glOrtho(0, width, height, 0, 1, -1)
    glMatrixMode(GL_MODELVIEW)

    this.init

    while (!isCloseRequested) {
      glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT)
      this.update
      Display.update
      sync(60)
    }

    destroy
  }

  def init: Unit = {}

  def update: Unit
}
package com.scan.nicol

import org.lwjgl.opengl._
import scala.actors._

trait Game extends Application with opengl.GLContext with Actor {
  val width = 800
  val height = 600

  start

  def act = {
    import Display._
    import GL11._

    setDisplayMode(new DisplayMode(width, height))
    create

    glEnable(GL_TEXTURE_2D)

    glMatrixMode(GL_PROJECTION)
    glLoadIdentity
    glOrtho(0, width, height, 0, 1, -1)
    glMatrixMode(GL_MODELVIEW)

    this.init

    while (!isCloseRequested) {
      this.update
      Display.update
    }

    destroy
  }

  def init: Unit = {}
  def update: Unit
}
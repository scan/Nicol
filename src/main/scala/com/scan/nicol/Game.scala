package com.scan.nicol

import org.lwjgl.opengl._
import org.lwjgl.Sys._
import scala.actors._

abstract class Game(title: String, width: Int = 800, height: Int = 600) extends Application with Actor {
  def size = (width, height)

  start

  def act = {
    import Display._

    setDisplayMode(new DisplayMode(width, height))
    setTitle(title)
    create

    import GL11._

    glEnable(GL_TEXTURE_2D)

    glShadeModel(GL_SMOOTH)
    glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST)

    glClearDepth(1.0f)
    glDepthFunc(GL_LEQUAL)
    glEnable(GL_DEPTH_TEST)

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
package com.scan.nicol

import org.lwjgl.opengl._
import org.lwjgl._
import scala.actors._
import opengl.GLUtils._

abstract class Game(title: String, width: Int = 800, height: Int = 600) extends Actor {

  import Display._

  def size = (width, height)

  def main(args: Array[String]) = start

  import opengl.Renderer

  def draw[A](that: A, position: (Float, Float) = (0, 0), rgb: (Float, Float, Float) = (1, 1, 1))(implicit renderer: Renderer[A]) = renderer.draw(that, position._1, position._2, rgb)

  def act = {
    setDisplayMode(new DisplayMode(width, height))
    setTitle(title)
    create

    import GL11._

    glEnable(GL_TEXTURE_2D)
    glDisable(GL_DEPTH_TEST)
    glDisable(GL_LIGHTING)

    glShadeModel(GL_SMOOTH)
    glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_FASTEST)

    glEnable(GL_BLEND)
    glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)

    glMatrixMode(GL_PROJECTION)
    glLoadIdentity
    glOrtho(0, width, height, 0, 1, -1)
    glMatrixMode(GL_MODELVIEW)

    this.init

    while (!isCloseRequested) {
      glClear(GL_COLOR_BUFFER_BIT)
      glLoadIdentity

      this.update
      Display.update
      updateFPS
    }

    end

    destroy
  }

  import Sys._

  private var lastFPS = time
  private var fps = 0

  private def time = (getTime * 1000) / getTimerResolution

  private def updateFPS = {
    if (time - lastFPS > 1000) {
      setTitle(title + " [" + fps + "]")
      fps = 0
      lastFPS = time
    }
    fps += 1
  }

  def sync = Display.sync _

  def init: Unit = {}

  def update: Unit

  def end: Unit = {}
}
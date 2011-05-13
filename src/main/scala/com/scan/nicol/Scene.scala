package com.scan.nicol

import opengl.Renderer
import org.lwjgl.opengl.{GL11, DisplayMode, Display}
import org.lwjgl.opengl.GL11._
import org.lwjgl.Sys
import org.lwjgl.Sys._
import org.lwjgl.opengl.Display._

/**
 * A Scene is an object that has a state and is a master to many other object. There may not be more than one Scene active
 * at the same time.
 */
trait Scene extends Mutable {
  def run: Scene
}

abstract class EntryScene(title: String, width: Int = 800, height: Int = 600)(init: => Scene) extends Scene {

  import Display._

  def run = {
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

    init
  }
}

abstract class EndScene(end: => Unit) extends Scene {
  def run = {
    end
    Display.destroy
    null
  }
}

/**
 * An end object without cleanup.
 */
object End extends EndScene(() => ())

abstract class GameScene extends Scene {
  def draw[A](that: A, position: (Float, Float) = (0, 0), rgb: (Float, Float, Float) = (1, 1, 1))(implicit renderer: Renderer[A]) = renderer.draw(that, position._1, position._2, rgb)

  def update: Scene

  def run = {
    import GL11._
    glClear(GL_COLOR_BUFFER_BIT)
    glLoadIdentity

    val r = this.update

    Display.update
    updateFPS

    if (r == null) this else r
  }

  import Sys._

  private var lastFPS = time
  private var fps = 0

  private def time = (getTime * 1000) / getTimerResolution

  private lazy val title = Display.getTitle

  private def updateFPS = {
    if (time - lastFPS > 1000) {
      Display.setTitle(title + " [" + fps + "]")
      fps = 0
      lastFPS = time
    }
    fps += 1
  }

  def sync = Display.sync _
}
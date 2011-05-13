package com.scan.nicol

import opengl.Renderer
import org.lwjgl.opengl.{Display, GL11}
import org.lwjgl.Sys

/**
 * This is the base class for any non-empty scene, any real game scene. You just override update to get a nice custom scene.
 */
abstract class GameScene extends Scene {
  def draw[A](that: A, position: (Float, Float) = (0, 0), rgb: (Float, Float, Float) = (1, 1, 1))(implicit renderer: Renderer[A]) = renderer.draw(that, position._1, position._2, rgb)

  def update: Scene

  def run = {
    import GL11._
    glClear(GL_COLOR_BUFFER_BIT)
    glLoadIdentity

    val r = this.update

    Display.update

    if (r == null) this else r
  }
}

object GameScene {
  /**
   * This should be used to make small scenes. For bigger ones, a real GameScene child is recommended.
   */
  def apply(f: => Scene) = new GameScene {
    @inline
    def update = f
  }
}

/**
 * A mixin you can use if you need a `sync` method to keep within a certain fps. Also enables showing the
 * current FPS in the title bar.
 */
trait SyncableScene {
  val targetFPS = 60

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

  def sync = {
    Display.sync(targetFPS)
    updateFPS
  }
}
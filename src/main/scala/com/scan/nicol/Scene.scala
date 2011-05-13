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
  scene =>

  def run: Scene

  /**
   * The follow combinator makes a scene that first runs this scene, then the other,
   * ignoring the returned Scene of this run.
   */
  def >:>(that: Scene) = Scene({
    scene.run
    that.run
  })

  /**
   * The followedBy combinator makes a scene that first runs the other scene, then this,
   * ignoring the returned Scene that other scene.
   */
  def <:<(that: Scene) = Scene({
    that.run
    scene.run
  })
}

object Scene {
  /**
   * So you don't have to create a subclass all the time.
   */
  def apply(f: => Scene) = new Scene {
    @inline
    def run = f
  }
}

object EntryScene {
  /**
   * This should be the initial scene of the game. No drawing will be possible if this scene has not happened.
   */
  def apply(title: String, width: Int = 800, height: Int = 600) = Scene {
    import Display._

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

    null
  }
}

object EndScene {
  def apply(end: => Unit) = Scene {
    end
    Display.destroy
    null
  }
}

/**
 * An end object without cleanup.
 */
object End extends Scene {
  def run = {
    Display.destroy
    null
  }
}
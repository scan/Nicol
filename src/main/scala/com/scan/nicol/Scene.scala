package com.scan.nicol

import org.lwjgl.opengl.{GL11, DisplayMode, Display}
import org.lwjgl.opengl.GL11._
import org.lwjgl.Sys
import org.lwjgl.Sys._
import org.lwjgl.opengl.Display._
import scala.Some

/**
 * A Scene is an object that has a state and is a master to many other object. There may not be more than one Scene active
 * at the same time.
 */
trait Scene extends Function0[Unit] with Mutable {
  def >>(that: Scene) = Scene {
    this.apply
    that.apply
  }
}

object Scene {
  def apply(body: => Unit): Scene = new Scene {
    def apply: Unit = body
  }

  implicit def asOption(s:Scene): Option[Scene] = Some(s)
}

case class Init(title: String, width: Int = 800, height: Int = 600) extends Scene {
  def apply = {
    import Display._

    setDisplayMode(new DisplayMode(width, height))
    setTitle(title)
    create

    import GL11._

    glEnable(GL_TEXTURE_2D)

    glDisable(GL_LIGHTING)
    glDisable(GL_DEPTH_TEST)

    glShadeModel(GL_SMOOTH)
    glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_FASTEST)

    glEnable(GL_BLEND)
    glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)

    glMatrixMode(GL_PROJECTION)
    glLoadIdentity
    glOrtho(0, width, height, 0, 1, -1)
    glMatrixMode(GL_MODELVIEW)
    glLoadIdentity
  }
}

object End extends Scene {
  def apply = {
    Display.destroy
  }
}

trait StaticScene extends Scene {
  def run: Unit

  def apply = run
}

trait LoopScene extends Scene {
  val clearColour: (Float, Float, Float, Float) = (0, 0, 0, 0)

  def apply = {
    var next: Option[Scene] = None

    while (next == None) {
      GL11.glClearColor(clearColour._1, clearColour._2, clearColour._3, clearColour._4)
      GL11.glClear(GL11.GL_COLOR_BUFFER_BIT)
      GL11.glLoadIdentity

      next = update
      Display.update
    }

    next.getOrElse(() => ()).apply
  }

  def update: Option[Scene]
}
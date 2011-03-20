package com.scan.nicol.opengl

import org.lwjgl.opengl.GL11._

trait GLContext {
  def glDrawingContext(mode: Int)(body: => Unit) = {
    glBegin(mode)
    body
    glEnd
  }
}
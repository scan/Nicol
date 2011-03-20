package com.scan.nicol

import opengl._
import org.lwjgl.opengl._

object App extends Game {
  import GL11._

  def update = {
    glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT)

    glColor3f(1f, 1f, 1f)

    Texture("sika.png").bind

    glDrawingContext(GL_QUADS) {
      glTexCoord2f(0, 0)
      glVertex2f(100, 100)
      glTexCoord2f(Texture("sika.png").width, 0)
      glVertex2f(100 + Texture("sika.png").imageSize._1, 100)
      glTexCoord2f(Texture("sika.png").width, Texture("sika.png").height)
      glVertex2f(100 + Texture("sika.png").imageSize._1, 100 + Texture("sika.png").imageSize._2)
      glTexCoord2f(0, Texture("sika.png").height)
      glVertex2f(100, 100 + Texture("sika.png").imageSize._2)
    }
  }
}
package com.scan.nicol
package renderer

import font._

trait StringRenderer {

  import Font.arial._

  implicit object StringRenderer extends Renderer[String] {
    def draw(that: String, x: Float, y: Float, colour: (Float, Float, Float) = (1, 1, 1)) =
      write(that, (x, y), colour)
  }

  object CenteredStringRenderer extends Renderer[String] {
    def draw(that: String, x: Float, y: Float, colour: (Float, Float, Float) = (1, 1, 1)) =
      write(that, (x - stringWidth(that) / 2, y - height / 2), colour)
  }

  object RightStringRenderer extends Renderer[String] {
    def draw(that: String, x: Float, y: Float, colour: (Float, Float, Float) = (1, 1, 1)) =
      write(that, (x - stringWidth(that), y), colour)
  }

}
package com.scan.nicol
package renderer

import font._

/**
 * Some Renderers to use when you want to write Strings
 */
trait StringRenderer {

  import Font.arial

  implicit object ArialStringRenderer extends StringRenderer(arial)

  object ArialCenderedStringRenderer extends CenteredStringRenderer(arial)

  object ArialRightStringRenderer extends RightStringRenderer(arial)

  case class StringRenderer(font: Font) extends Renderer[String] {
    def draw(that: String, x: Float, y: Float, colour: (Float, Float, Float) = (1, 1, 1)) =
      font.write(that, (x, y), colour)
  }

  /**
   * Centers the String around the given position
   */
  case class CenteredStringRenderer(font: Font) extends Renderer[String] {
    def draw(that: String, x: Float, y: Float, colour: (Float, Float, Float) = (1, 1, 1)) =
      font.write(that, (x - font.stringWidth(that) / 2, y - font.height / 2), colour)
  }

  /**
   * Writes the String left from the given position
   */
  case class RightStringRenderer(font: Font) extends Renderer[String] {
    def draw(that: String, x: Float, y: Float, colour: (Float, Float, Float) = (1, 1, 1)) =
      font.write(that, (x - font.stringWidth(that), y), colour)
  }

}
package nicol
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

  /**
   * Draws a string with the given font, boringly left-aligned.
   */
  case class StringRenderer(font: Font) extends Renderer[String] {
    def draw(that: String, position: (Float, Float), rgb: (Float, Float, Float), rotation: Float, offset: (Float, Float)) =
      font.write(that, position, rgb, rotation)
  }

  /**
   * Centers the String around the given position
   */
  case class CenteredStringRenderer(font: Font) extends Renderer[String] {
    def draw(that: String, position: (Float, Float), rgb: (Float, Float, Float), rotation: Float, offset: (Float, Float)) =
      font.write(that, (position._1 - font.stringWidth(that) / 2, position._2 - font.height / 2), rgb, rotation)
  }

  /**
   * Writes the String left from the given position
   */
  case class RightStringRenderer(font: Font) extends Renderer[String] {
    def draw(that: String, position: (Float, Float), rgb: (Float, Float, Float), rotation: Float, offset: (Float, Float)) =
      font.write(that, (position._1 - font.stringWidth(that), position._2), rgb, rotation)
  }

}
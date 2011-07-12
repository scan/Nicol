package nicol.input

import org.lwjgl.input.Mouse._
import org.lwjgl.opengl.Display

/**
 * This object represents the mouse. Nicol makes some assumptions about it,
 * so we assume here that there is exactly one connected to the computer, and
 * it only moves in two dimensions. Also, it has three buttons.
 * Mouse and Cursor are synonimous here.
 */
object Mouse {

  trait Button {
    private[nicol] val button: Int
  }

  case object Left extends Button {
    private[nicol] val button = 0
  }

  case object Right extends Button {
    private[nicol] val button = 1
  }

  case object Middle extends Button {
    private[nicol] val button = 2
  }

  /**
   * Grabs the mouse and keeps it within the window.
   */
  def grab = setGrabbed(true)

  /**
   * Sets the mouse free from the window.
   */
  def release = setGrabbed(false)

  def apply: (Float, Float) = (getX, Display.getDisplayMode.getHeight - getY)

  def apply(btn: Button): Boolean = isButtonDown(btn.button)

  /**
   * This changes the position of the cursor on screen. Use with care.
   */
  def apply(x: Float, y: Float) = setCursorPosition(x.toInt, Display.getDisplayMode.getHeight - y.toInt); updateCursor
}

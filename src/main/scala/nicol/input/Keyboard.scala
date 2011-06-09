package nicol.input

import org.lwjgl.input.Keyboard._
import java.util.{Timer, TimerTask}

/**
 * A Key is, not very surprisingly, anything you can find in front of you,
 * on your keyboard.
 */
object Key {
  private val keys = Map (
    // Special Events
    "escape" -> KEY_ESCAPE,
    "enter" -> KEY_RETURN,
    "space" ->  KEY_SPACE,
    "lshift" ->  KEY_LSHIFT,
    "rshift" ->  KEY_RSHIFT,
    "lctrl" ->  KEY_LCONTROL,
    "rctrl" ->  KEY_RCONTROL,
    "lmeta" ->  KEY_LMETA,
    "rmeta" ->  KEY_RMETA,
    // Arrow Keys
    "up" ->   KEY_UP,
    "down" -> KEY_DOWN,
    "left" -> KEY_LEFT,
    "right" ->  KEY_RIGHT,
    // Function Keys
    "F1" -> KEY_F1,
    "F2" -> KEY_F2,
    "F3" -> KEY_F3,
    "F4" -> KEY_F4,
    "F5" -> KEY_F5,
    "F6" -> KEY_F6,
    "F7" -> KEY_F7,
    "F8" -> KEY_F8,
    "F9" -> KEY_F9,
    "F10" -> KEY_F10,
    "F11" -> KEY_F11,
    "F12" -> KEY_F12,
    "F13" -> KEY_F13,
    "F14" -> KEY_F14,
    "F15" -> KEY_F15,
    // Letters
    "a" -> KEY_A,
    "b" -> KEY_B,
    "c" -> KEY_C,
    "d" -> KEY_D,
    "e" -> KEY_E,
    "f" -> KEY_F,
    "g" -> KEY_G,
    "h" -> KEY_H,
    "i" -> KEY_I,
    "j" -> KEY_J,
    "k" -> KEY_K,
    "l" -> KEY_L,
    "m" -> KEY_M,
    "n" -> KEY_N,
    "o" -> KEY_O,
    "p" -> KEY_P,
    "q" -> KEY_Q,
    "r" -> KEY_R,
    "s" -> KEY_S,
    "t" -> KEY_T,
    "u" -> KEY_U,
    "v" -> KEY_V,
    "w" -> KEY_W,
    "x" -> KEY_X,
    "y" -> KEY_Y,
    "z" -> KEY_Z,
    // Numbers
    "0" -> KEY_0,
    "1" -> KEY_1,
    "2" -> KEY_2,
    "3" -> KEY_3,
    "4" -> KEY_4,
    "5" -> KEY_5,
    "6" -> KEY_6,
    "7" -> KEY_7,
    "8" -> KEY_8,
    "9" -> KEY_9,
    // Other Keys
    "-" -> KEY_MINUS,
    "+" -> KEY_ADD,
    "=" -> KEY_EQUALS,
    "\t" -> KEY_TAB,
    "[" -> KEY_LBRACKET,
    "]" -> KEY_RBRACKET,
    ";" -> KEY_SEMICOLON,
    "'" -> KEY_APOSTROPHE,
    "\\" -> KEY_BACKSLASH,
    "," -> KEY_COMMA,
    "." -> KEY_PERIOD,
    "/" -> KEY_SLASH,
    "*" -> KEY_MULTIPLY,
    ":" -> KEY_COLON,
    "_" -> KEY_UNDERLINE,
    "none" -> KEY_NONE
  )

  def keyDown(key: String) = {
    val event = keys.get(key).getOrElse(KEY_NONE)
    isKeyDown(event)
  }

  def keyEvent[A](consumer: KeyEvent => Option[A]): Option[A] = {
    if (next) {
      val eventid = getEventKey
      val name = keys.find(_._2 == eventid).map(_._1).getOrElse("none")
      val state = getEventKeyState
      val event = KeyEvent(eventid, name, state)
      consumer(event)
    } else None
  }

  /** Enable / Disable Event key repeats */
  def repeat = enableRepeatEvents _

  /** Is Repeat events enabled */
  def isRepeat = areRepeatEventsEnabled

  /**
   * Gets if a standard char key is pressed.
   * Uppercase and lowercase letters make no difference here.
   */
  def char(a: Char) = keyDown(a.toLower.toString)

  /**
   * Tests if one of the F keys is pressed.
   */
  def F(n: Int) = if (n < 1 || n > 15) {
    keyDown(n.toString)
  } else false

  /**
   * Any enter / return keys.
   */
  def enter = keyDown("enter")

  /**
   * Any of the shift keys.
   */
  def shift = keyDown("lshift") || keyDown("rshift")

  /**
   * Any of the CTRL keys.
   */
  def ctrl = keyDown("lctrl") || keyDown("rctrl")

  /**
   * Any of the WIN or META keys.
   */
  def meta = keyDown("lmeta") || keyDown("rmeta")

  /**
   * Space key.
   */
  def space = keyDown("space")

  /**
   * The up arrow.
   */
  def up = keyDown("up")

  /**
   * The down arrow.
   */
  def down = keyDown("down")

  /**
   * The left arrow.
   */
  def left = keyDown("left")

  /**
   * The right arrow.
   */
  def right = keyDown("right")

  /**
   * The Esc key.
   */
  def escape = keyDown("escape")
}

private [nicol] case class KeyEvent(id: Int, name: String, state: Boolean) {
  def pressed[A] (consumer: String => A): Option[A] = {
    if (state) handler(consumer) else None
  }

  def released[A] (consumer: String => A): Option[A] = {
    if (!state) handler(consumer) else None
  }

  private def handler[A] (consumer: String => A): Option[A] = try {
    Some(consumer(name))
  } catch {
    case e: MatchError => None
  }
}

private[nicol] object Keyboard {
  def poll = org.lwjgl.input.Keyboard.poll

  def apply(key: Int): Boolean = org.lwjgl.input.Keyboard.isKeyDown(key)
}

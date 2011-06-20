package nicol.input

import org.lwjgl.input.Keyboard._

/**
 * A Key is, not very surprisingly, anything you can find in front of you,
 * on your keyboard.
 */
trait Key {
  private[nicol] def getKey: Int

  def apply: Boolean = Key.keyDown(this)

  def or(that: Key) = new Key {
    def getKey = KEY_NONE

    override def apply: Boolean = this.apply || that.apply
  }

  def and(that: Key) = new Key {
    def getKey = KEY_NONE

    override def apply: Boolean = this.apply && that.apply
  }

  def name = toString
}

object Key {

  implicit def keyToBool(k: Key): Boolean = k.apply

  private case class key(k: Int) extends Key {
    def getKey = k
  }

  case class or(k1: Key, k2: Key) extends Key {
    def getKey = KEY_NONE

    override def apply: Boolean = k1.apply || k2.apply
  }

  case class and(k1: Key, k2: Key) extends Key {
    def getKey = KEY_NONE

    override def apply: Boolean = k1.apply && k2.apply
  }

  private val keys = Map(
    // Special Events
    "escape" -> key(KEY_ESCAPE),
    "enter" -> key(KEY_RETURN),
    "space" -> key(KEY_SPACE),
    "lshift" -> key(KEY_LSHIFT),
    "rshift" -> key(KEY_RSHIFT),
    "lctrl" -> key(KEY_LCONTROL),
    "rctrl" -> key(KEY_RCONTROL),
    "lmeta" -> key(KEY_LMETA),
    "rmeta" -> key(KEY_RMETA),
    // Arrow Keys
    "up" -> key(KEY_UP),
    "down" -> key(KEY_DOWN),
    "left" -> key(KEY_LEFT),
    "right" -> key(KEY_RIGHT),
    // Function Keys
    "F1" -> key(KEY_F1),
    "F2" -> key(KEY_F2),
    "F3" -> key(KEY_F3),
    "F4" -> key(KEY_F4),
    "F5" -> key(KEY_F5),
    "F6" -> key(KEY_F6),
    "F7" -> key(KEY_F7),
    "F8" -> key(KEY_F8),
    "F9" -> key(KEY_F9),
    "F10" -> key(KEY_F10),
    "F11" -> key(KEY_F11),
    "F12" -> key(KEY_F12),
    "F13" -> key(KEY_F13),
    "F14" -> key(KEY_F14),
    "F15" -> key(KEY_F15),
    // Letters
    "a" -> key(KEY_A),
    "b" -> key(KEY_B),
    "c" -> key(KEY_C),
    "d" -> key(KEY_D),
    "e" -> key(KEY_E),
    "f" -> key(KEY_F),
    "g" -> key(KEY_G),
    "h" -> key(KEY_H),
    "i" -> key(KEY_I),
    "j" -> key(KEY_J),
    "k" -> key(KEY_K),
    "l" -> key(KEY_L),
    "m" -> key(KEY_M),
    "n" -> key(KEY_N),
    "o" -> key(KEY_O),
    "p" -> key(KEY_P),
    "q" -> key(KEY_Q),
    "r" -> key(KEY_R),
    "s" -> key(KEY_S),
    "t" -> key(KEY_T),
    "u" -> key(KEY_U),
    "v" -> key(KEY_V),
    "w" -> key(KEY_W),
    "x" -> key(KEY_X),
    "y" -> key(KEY_Y),
    "z" -> key(KEY_Z),
    // Numbers
    "0" -> key(KEY_0),
    "1" -> key(KEY_1),
    "2" -> key(KEY_2),
    "3" -> key(KEY_3),
    "4" -> key(KEY_4),
    "5" -> key(KEY_5),
    "6" -> key(KEY_6),
    "7" -> key(KEY_7),
    "8" -> key(KEY_8),
    "9" -> key(KEY_9),
    // Other Keys
    "-" -> key(KEY_MINUS),
    "+" -> key(KEY_ADD),
    "=" -> key(KEY_EQUALS),
    "\t" -> key(KEY_TAB),
    "[" -> key(KEY_LBRACKET),
    "]" -> key(KEY_RBRACKET),
    ";" -> key(KEY_SEMICOLON),
    "'" -> key(KEY_APOSTROPHE),
    "\\" -> key(KEY_BACKSLASH),
    "," -> key(KEY_COMMA),
    "." -> key(KEY_PERIOD),
    "/" -> key(KEY_SLASH),
    "*" -> key(KEY_MULTIPLY),
    ":" -> key(KEY_COLON),
    "_" -> key(KEY_UNDERLINE),
    "none" -> key(KEY_NONE)
  )

  private[nicol] def keyDown(key: Key) = isKeyDown(key.getKey)

  def keyEvent[A](consumer: KeyEvent => Option[A]): Option[A] = {
    if (next) {
      val eventid = getEventKey
      val name = key(eventid)
      val state = getEventKeyState
      val event = KeyEvent(eventid, name, state)
      consumer(event)
    } else None
  }

  /**Enable / Disable Event key repeats */
  def repeat = enableRepeatEvents _

  /**Is Repeat events enabled */
  def isRepeat = areRepeatEventsEnabled

  /**
   * Gets if a standard char key is pressed.
   * Uppercase and lowercase letters make no difference here.
   */
  def char(a: Char): Key = keys.getOrElse(a.toLower.toString, none)

  /**
   * Tests if one of the F keys is pressed.
   */
  def F(n: Int): Key = if (n < 1 || n > 15) {
    keys.getOrElse("F%d".format(n), none)
  } else none

  /**
   * Any enter / return keys.
   */
  case object enter extends Key {
    val getKey = KEY_RETURN
  }

  case object lshift extends Key {
    val getKey = KEY_LSHIFT
  }

  case object rshift extends Key {
    val getKey = KEY_RSHIFT
  }

  /**
   * Any of the shift keys.
   */
  object shift extends or(lshift, rshift)

  case object lctrl extends Key {
    val getKey = KEY_LCONTROL
  }

  case object rctrl extends Key {
    val getKey = KEY_RCONTROL
  }

  /**
   * Any of the CTRL keys.
   */
  object ctrl extends or(lctrl, rctrl)

  case object lmeta extends Key {
    val getKey = KEY_LMETA
  }

  case object rmeta extends Key {
    val getKey = KEY_RMETA
  }

  /**
   * Any of the WIN or META keys.
   */
  object meta extends or(lmeta, rmeta)

  /**
   * Space key.
   */
  case object space extends Key {
    val getKey = KEY_SPACE
  }

  /**
   * The up arrow.
   */
  case object up extends Key {
    val getKey = KEY_UP
  }

  /**
   * The down arrow.
   */
  case object down extends Key {
    val getKey = KEY_DOWN
  }

  /**
   * The left arrow.
   */
  case object left extends Key {
    val getKey = KEY_LEFT
  }

  /**
   * The right arrow.
   */
  case object right extends Key {
    val getKey = KEY_RIGHT
  }

  /**
   * The Esc key.
   */
  case object escape extends Key {
    val getKey = KEY_ESCAPE
  }

  /**
   * The key that is never pressed. Never. Never ever.
   */
  object none extends Key {
    val getKey = KEY_NONE

    override def apply: Boolean = false
  }

}

private[nicol] case class KeyEvent(id: Int, key: Key, state: Boolean) {
  def pressed[A](consumer: Key => A): Option[A] = {
    if (state) handler(consumer) else None
  }

  def released[A](consumer: Key => A): Option[A] = {
    if (!state) handler(consumer) else None
  }

  private def handler[A](consumer: Key => A): Option[A] = try {
    Some(consumer(key))
  } catch {
    case e: MatchError => None
  }

  def name = key.name
}

private[nicol] object Keyboard {
  def poll = org.lwjgl.input.Keyboard.poll

  def apply(key: Int): Boolean = org.lwjgl.input.Keyboard.isKeyDown(key)
}

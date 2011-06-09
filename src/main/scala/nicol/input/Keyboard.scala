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
    KEY_ESCAPE -> "escape",
    KEY_RETURN ->"enter",
    KEY_SPACE -> "space",
    KEY_LSHIFT -> "shift",
    KEY_RSHIFT -> "shift",
    KEY_LCONTROL -> "ctrl",
    KEY_RCONTROL -> "ctrl",
    KEY_LMETA -> "meta",
    KEY_RMETA -> "meta",
    // Arrow Keys
    KEY_UP -> "up",
    KEY_DOWN -> "down",
    KEY_LEFT -> "left",
    KEY_RIGHT -> "right",
    // Function Keys
    KEY_F1 -> "F1",
    KEY_F2 -> "F2",
    KEY_F3 -> "F3",
    KEY_F4 -> "F4",
    KEY_F5 -> "F5",
    KEY_F6 -> "F6",
    KEY_F7 -> "F7",
    KEY_F8 -> "F8",
    KEY_F9 -> "F9",
    KEY_F10 -> "F10",
    KEY_F11 -> "F11",
    KEY_F12 -> "F12",
    KEY_F13 -> "F13",
    KEY_F14 -> "F14",
    KEY_F15 -> "F15",
    KEY_A -> "a",
    KEY_B -> "b",
    KEY_C -> "c",
    KEY_D -> "d",
    KEY_E -> "e",
    KEY_F -> "f",
    KEY_G -> "g",
    KEY_H -> "h",
    KEY_I -> "i",
    KEY_J -> "j",
    KEY_K -> "k",
    KEY_L -> "l",
    KEY_M -> "m",
    KEY_N -> "n",
    KEY_O -> "o",
    KEY_P -> "p",
    KEY_Q -> "q",
    KEY_R -> "r",
    KEY_S -> "s",
    KEY_T -> "t",
    KEY_U -> "u",
    KEY_V -> "v",
    KEY_W -> "w",
    KEY_X -> "x",
    KEY_Y -> "y",
    KEY_Z -> "z",
    // Numbers
    KEY_0 -> "0",
    KEY_1 -> "1",
    KEY_2 -> "2",
    KEY_3 -> "3",
    KEY_4 -> "4",
    KEY_5 -> "5",
    KEY_6 -> "6",
    KEY_7 -> "7",
    KEY_8 -> "8",
    KEY_9 -> "9",
    // Other Keys
    KEY_MINUS -> "-",
    KEY_ADD -> "+",
    KEY_EQUALS -> "=",
    KEY_TAB -> "\t",
    KEY_LBRACKET -> "[",
    KEY_RBRACKET -> "]",
    KEY_SEMICOLON -> ";",
    KEY_APOSTROPHE -> "'",
    KEY_BACKSLASH -> "\\",
    KEY_COMMA -> ",",
    KEY_PERIOD -> ".",
    KEY_SLASH -> "/",
    KEY_MULTIPLY -> "*",
    KEY_COLON -> ":",
    KEY_UNDERLINE -> "_"
  )

  private val ready = collection.mutable.Map[Int, Boolean](
    keys.keys.map((_, true)).toList: _*
  )
 
  private var nexted = false
 
  private def delay(event: Int, interval: Long) {
    val timer = new Timer
    timer.schedule(new TimerTask {
      def run = ready(event) = true 
    }, interval)
  }

  def clearDelays = 
    ready.filter(_._2 == false).foreach(t => ready(t._1) = true)

  def keyDown(key: String, interval: Long = 0) = {
    val event = keys.find(_._2 == key).map(_._1).getOrElse(KEY_NONE)
    if (ready(event)) {
      val pressed = isKeyDown(event)
      if (pressed && interval > 0) {
        ready(event) = false
        delay(event, interval)
      }
      pressed
    } else false
  }

  def keyPressed[A](partial: String => A): Option[A] = {
    if (getEventKeyState) handler(partial)
    else if (next && getEventKeyState) handler(partial)
    else None 
  }

  private def handler[A](partial: String => A): Option[A] = try {
    Some(partial(keys(getEventKey)))
  } catch {
    case e: MatchError => None
  }

  /** Enable / Disable Event key repeats */
  def repeat = enableRepeatEvents _

  /** Is Repeat events enabled */
  def isRepeat = areRepeatEventsEnabled
}

private[nicol] object Keyboard {
  def poll = org.lwjgl.input.Keyboard.poll

  def apply(key: Int): Boolean = org.lwjgl.input.Keyboard.isKeyDown(key)
}

package com.scan.nicol.input

import org.lwjgl.input.Keyboard._

object Key {

  // This is awful, but LWJGL keys are not consecutive values
  def char(c: Char) = Keyboard(c.toLower match {
    case 'a' => KEY_A
    case 'b' => KEY_B
    case 'c' => KEY_C
    case 'd' => KEY_D
    case 'e' => KEY_E
    case 'f' => KEY_F
    case 'g' => KEY_G
    case 'h' => KEY_H
    case 'i' => KEY_I
    case 'j' => KEY_J
    case 'k' => KEY_K
    case 'l' => KEY_L
    case 'm' => KEY_M
    case 'n' => KEY_N
    case 'o' => KEY_O
    case 'p' => KEY_P
    case 'q' => KEY_Q
    case 'r' => KEY_R
    case 's' => KEY_S
    case 't' => KEY_T
    case 'u' => KEY_U
    case 'v' => KEY_V
    case 'w' => KEY_W
    case 'x' => KEY_X
    case 'y' => KEY_Y
    case 'z' => KEY_Z

    case '0' => KEY_0
    case '1' => KEY_1
    case '2' => KEY_2
    case '3' => KEY_3
    case '4' => KEY_4
    case '5' => KEY_5
    case '6' => KEY_6
    case '7' => KEY_7
    case '8' => KEY_8
    case '9' => KEY_9

    case '-' => KEY_MINUS
    case '+' => KEY_ADD
    case '=' => KEY_EQUALS
    case '\t' => KEY_TAB
    case '\n' => KEY_RETURN
    case '(' => KEY_LBRACKET
    case ')' => KEY_RBRACKET
    case ';' => KEY_SEMICOLON
    case '\'' => KEY_APOSTROPHE
    case '\\' => KEY_BACKSLASH
    case ',' => KEY_COMMA
    case '.' => KEY_PERIOD
    case '/' => KEY_SLASH
    case '*' => KEY_MULTIPLY
    case ' ' => KEY_SPACE
    case ':' => KEY_COLON
    case '_' => KEY_UNDERLINE

    case _ => KEY_NONE
  })

  def function(n: Int) = if (n < 1 || n > 15) {
    Keyboard(n match {
      case 1 => KEY_F1
      case 2 => KEY_F2
      case 3 => KEY_F3
      case 4 => KEY_F4
      case 5 => KEY_F5
      case 6 => KEY_F6
      case 7 => KEY_F7
      case 8 => KEY_F8
      case 9 => KEY_F9
      case 10 => KEY_F10
      case 11 => KEY_F11
      case 12 => KEY_F12
      case 13 => KEY_F13
      case 14 => KEY_F14
      case 15 => KEY_F15
    })
  } else false

  def shift = Keyboard(KEY_LSHIFT) || Keyboard(KEY_RSHIFT)

  def ctrl = Keyboard(KEY_LCONTROL) || Keyboard(KEY_RCONTROL)

  def space = Keyboard(KEY_SPACE)

  def up = Keyboard(KEY_UP)

  def down = Keyboard(KEY_DOWN)

  def left = Keyboard(KEY_LEFT)

  def right = Keyboard(KEY_RIGHT)

  def apply(c: Char) = char(c)
}

private object Keyboard {
  def apply(key: Int): Boolean = org.lwjgl.input.Keyboard.isKeyDown(key)
}
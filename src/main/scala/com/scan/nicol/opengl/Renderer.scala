package com.scan.nicol.opengl

trait Renderer[A] {
  // def draw(that: A, pos: (Float, Float) = (0, 0), colour: (Float, Float, Float) = (1, 1, 1)): Unit = draw(that, pos._1, pos._2, colour)

  def draw(that: A, x: Float = 0, y: Float = 0, colour: (Float, Float, Float) = (1, 1, 1)): Unit
}
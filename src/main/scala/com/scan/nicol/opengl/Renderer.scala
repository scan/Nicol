package com.scan.nicol.opengl

trait Renderer[A] {
  def draw(that: A, x: Float, y: Float, colour: (Float, Float, Float) = (1, 1, 1)): Unit
}
package com.scan.nicol.renderer

trait Renderer[A] {
  def draw(that: A, x: Float = 0, y: Float = 0, colour: (Float, Float, Float) = (1, 1, 1)): Unit
}

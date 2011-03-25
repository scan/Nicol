package com.scan.nicol.opengl

trait Renderer[A] {
  def draw(that: A, x: Float, y: Float): Unit
}
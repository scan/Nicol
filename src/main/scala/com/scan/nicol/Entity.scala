package com.scan.nicol

trait Entity extends Mutable {
  def update
}

trait DrawableEntity extends Entity {
  def draw(x: Float, y: Float)
}
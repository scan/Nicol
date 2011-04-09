package com.scan.nicol

trait Entity extends Mutable {
  def update
}

trait DrawableEntity extends Entity {
  def draw(x: Float, y: Float)
}

object DrawableEntity {

  import opengl._

  implicit object EntityRenderer extends Renderer[DrawableEntity] {
    def draw(that: DrawableEntity, x: Float, y: Float, rgb: (Float, Float, Float)) = that.draw(x, y)
  }

}
package com.scan.nicol

trait Entity extends Mutable {
  def update
}

trait RenderableEntity extends Entity with Renderable
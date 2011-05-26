package com.scan.nicol

import renderer._

trait StandardRenderer extends StandardGeometryRenderer with StringRenderer {

  implicit object ImageRenderer extends Renderer[Image] {
    def draw(that: Image, x: Float, y: Float, col: (Float, Float, Float)) = that.draw((x, y), 0, col)
  }

}
package nicol

import renderer._

trait StandardRenderer extends StandardGeometryRenderer with StringRenderer {

  implicit object ImageRenderer extends Renderer[Image] {
    def draw(that: Image, pos: (Float, Float), col: (Float, Float, Float), rot: Float, off: (Float, Float)) = that.draw(pos, 0, col, rot, 1, off)
  }

}
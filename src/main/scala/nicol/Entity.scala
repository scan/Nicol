package nicol

/**
 * An Entity is an unimportant base class for anything that should have an update
 * method. There is no real difference in using this trait yet.
 */
trait Entity extends Mutable {
  def update
}

/**
 * This is an entity that can be drawn also.
 */
trait DrawableEntity extends Entity {
  def draw(x: Float, y: Float)
}

object DrawableEntity {

  /**
   * Convenience object, so you can easily use [[GameScene.draw]] to draw these.
   */
  implicit object EntityRenderer extends renderer.Renderer[DrawableEntity] {
    def draw(that: DrawableEntity, x: Float, y: Float, rgb: (Float, Float, Float)) = that.draw(x, y)
  }

}
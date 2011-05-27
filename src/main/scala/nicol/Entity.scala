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
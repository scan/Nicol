package nicol.renderer

trait Renderer[A] {
  def draw(that: A, position: (Float, Float) = (0, 0), colour: (Float, Float, Float) = (1, 1, 1), rotation: Float = 0, offset: (Float, Float) = (0, 0))
}

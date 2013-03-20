package nicol

import org.jbox2d.common._

package object math {
  implicit def vectorToVec2(v: Vector) = new Vec2(v.x, v.y)

  implicit def vec2ToVector(v: Vec2) = new Vector(v.x, v.y)

  implicit def matrixToMat22(m: Matrix) = new Mat22(m.a, m.b, m.c, m.d)

  implicit def mat22ToMatrix(m: Mat22) = Matrix(m.ex.x, m.ey.x, m.ex.y, m.ey.y)

  implicit def float2Rot(a: Float) = new Rot(a)
}

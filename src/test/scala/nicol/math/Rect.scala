package nicol.math

import org.scalacheck._

object RectProps extends Properties("Rect") {

  import Prop._

  property("merge") = forAll((r1: Rect, r2: Rect) => {
    val r = (r1 + r2)
    r.collides(r1) && r.collides(r2)
  })
  
  import Gen._

  implicit lazy val arbRect = Arbitrary(for {
    x <- choose(-100, 100)
    y <- choose(-100, 100)
    w <- choose(0, 100)
    h <- choose(0, 100)
  } yield Rect(x, y, w, h))
}
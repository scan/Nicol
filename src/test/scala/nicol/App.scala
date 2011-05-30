package nicol

import input.Key._
import input.Mouse
import geom._
import math._

object App extends Game(Init("Nicol example App", 800, 600) >> Main)

object Main extends GameScene with SyncableScene with StandardRenderer with ShowFPS {
  scene =>

  import scala.math.{sin, cos}
  import scala.util.Random._

  lazy val image = Image("sika.png")

  val stars: Traversable[Line] = Seq.fill(1024) {
    val v = Vector(nextFloat * 800, nextFloat * 600)
    Line(v, v - (5, 5))
  }

  var (x, y) = (400, 300)
  var a = 0f

  var bullets = collection.mutable.ListBuffer[Bullet]()

  val camera = new View

  def update: Option[Scene] = {
    // for (n <- 0 to tileset.length - 1) tileset(n).draw((n * tileset.tileWidth, 0))
    if (left) a -= 0.1f
    if (right) a += 0.1f
    if (space) bullets += new Bullet(a)

    val (mx, my) = Mouse.apply

    camera.position += new Vector(
      if (mx <= 0) -1 else if (mx >= 799) 1 else 0,
      if (my <= 0) -1 else if (my >= 599) 1 else 0)

    bullets = bullets.filter(!_.finished)

    val r = 50f
    val redCircle = Circle((x, y), r)

    val targetCircle = Circle((
      x + (r * cos(a)).toFloat,
      y + (r * sin(a)).toFloat
      ), radius = r / 5
    )

    Pretransformed(draw(stars, rgb = (1, 1, 1)))

    camera {
      bullets.foreach {
        b =>
          b.update
          b.draw
      }

      draw(redCircle, rgb = (1, 0, 0))
      draw(targetCircle, rgb = (0, 1, 0))
      draw(image, position = (x - image.width / 2, y - image.height / 2), rotation = a)
    }

    Pretransformed {
      draw("Hello, Nicol!", position = (30, 30), rgb = (0.7f, 0.7f, 1f))
    }

    sync
    showFPS

    if (escape) End else None
  }

  class Bullet(d: Float) extends Entity {
    var (bx, by) = (x, y)
    var finished = false
    val speed = 10

    def update = {
      bx += (speed * cos(d)).toInt
      by += (speed * sin(d)).toInt
      finished = !camera.bounds.collides((bx, by))
    }

    def draw = {
      scene.draw(Circle((bx, by), 3), rgb = (250, 250, 210))(FilledCircleRenderer)
    }
  }

}
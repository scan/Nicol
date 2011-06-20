package nicol

import input.Key._
import input.Mouse
import geom._
import math._

object App extends Game(Init("Nicol example App", 800, 600) >> Main)

object Main extends BasicScene with ShowFPS {
  scene =>

  import scala.math.{sin, cos, Pi}
  import scala.util.Random._

  lazy val image = Image("sika.png")

  var (x, y) = (400, 300)

  var bullets = collection.mutable.ListBuffer[Bullet]()

  val vs = Seq.fill(1024)(Vector(nextFloat * 800, nextFloat * 600))

  val camera = new View

  Mouse.grabbed(true)

  def update: Option[Scene] = {

    val (mx, my) = Mouse.apply

    val point = Vector(mx, my)
    draw(Line(point, (point.x, point.y + 10)), rgb = (1f, 0f, 0f))
    draw(Line(point, (point.x + 10, point.y)), rgb = (1f, 0f, 0f))
    draw(Line(point, (point.x, point.y - 10)), rgb = (1f, 0f, 0f))
    draw(Line(point, (point.x - 10, point.y)), rgb = (1f, 0f, 0f))
    draw(Circle((mx - 0.5f, my + 0.5f), 5f), rgb = (1f, 0f, 0f))

    if (left) camera.position += Vector.left
    if (right) camera.position += Vector.right
    if (up) camera.position += Vector.up
    if (down) camera.position += Vector.down

    val a = Vector.right angle (Vector(mx, my) - Vector(400, 300))

    val stars: Traversable[Line] = vs.map {
      v => Line(v, v - Vector(cos(a).toFloat, sin(a).toFloat) * 5)
    }

    if (space) bullets += new Bullet(a)
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
      draw(a.toDegrees.toString, position = (30, 50))
      draw(camera.transform(point).toString, position = (30, 70))
    }

    sync
    showFPS

    keyEvent {
      e =>
        e released {
          case _ =>
            draw("Releasd %s".format(e.name), position = (350, 100), rgb = (1, 0, 0))
        }
        e pressed {
          case "escape" => End
          case "enter" => Paused
        }
    }
  }

  object Paused extends BasicScene {
    def update = {
      draw("Paused", position = (350, 300))
      sync

      keyEvent(_.pressed {
        case "escape" => End
        case "enter" => Main
      })
    }
  }

  class Bullet(d: Float) extends Entity {
    var (bx, by) = (x.toFloat, y.toFloat)
    var finished = false
    val speed = 10

    def update = {
      bx += (speed * cos(d)).toFloat
      by += (speed * sin(d)).toFloat
      finished = !camera.bounds.collides((bx.toInt, by.toInt))
    }

    def draw = {
      scene.draw(Circle((bx, by), 3), rgb = (250, 250, 210))(Filled)
    }
  }

}

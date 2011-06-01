package nicol

import scala.actors._

abstract class Game(entry: Scene) extends Actor {
	def main(args: Array[String]) = start

	def act = entry.apply
}

package com.scan.nicol

import scala.actors._

abstract class Game(entry: Scene) extends App with Actor {
  start

  def act = entry.apply
}
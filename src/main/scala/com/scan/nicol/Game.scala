package com.scan.nicol

import scala.actors._

abstract class Game(entry: EntryScene) extends Actor {
  private var curScene: Scene = entry

  def main(args: Array[String]) = start

  def act = {
    while (curScene != null)
      curScene = curScene.run
  }
}
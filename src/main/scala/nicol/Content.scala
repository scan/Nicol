package nicol

import opengl.Texture
import font.Font

object Content {
  private var sources = List(".")

  def load[A <: Resource](filename: String)(implicit loader: ResourceLoader[A]) = tryload(filename, sources)(loader)

  private def tryload[A <: Resource](filename: String, ls: List[String])(implicit loader: ResourceLoader[A]): Option[A] = ls match {
    case List() => None
    case x :: xs => loader(x + "/" + filename) orElse tryload(filename, xs)
  }

  def appendSource(source: String) = sources :+= source

  def prependSource(source: String) = sources ::= source

  import Texture._

  implicit object TextureLoader extends ResourceLoader[Texture] {
    def apply(res: String) = getTexture(res)
  }

  implicit object FontLoader extends ResourceLoader[Font] {
    def apply(res: String) = Some(Font(res, 15))
  }

}
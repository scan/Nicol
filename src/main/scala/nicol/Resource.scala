package nicol

trait Resource extends Immutable

trait ResourceLoader[A <: Resource] extends (String => Option[A])
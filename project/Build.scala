import sbt._

import Keys._

object General {
  val settings = Defaults.defaultSettings ++ Seq (
    organization := "com.github.scan",
    version := "0.1.1",
    publishTo := Some("name" at "url"),
    credentials += Credentials(Path.userHome / ".ivy2" / ".credentials")
  )
}

object NicolEngine extends Build {
  lazy val nicol = Project (
    "nicol", file("."), settings = General.settings
  )  aggregate (nicolCore)

 lazy val nicolCore = Project (
    "nicol-core", file("core"), settings = General.settings
  )
}
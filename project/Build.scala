import sbt._
import Keys._

object General {
  val settings = Defaults.defaultSettings ++ Seq(
    organization := "com.github.scan",
    version := "0.1.1",
    crossScalaVersions := Seq("2.9.0", "2.8.1"),
    credentials += Credentials(Path.userHome / ".ivy2" / ".credentials"),
    libraryDependencies += "org.scala-tools.testing" %% "scalacheck" % "1.8" % "test"
  )
}

object NicolEngine extends Build {
  lazy val nicol = Project(
    "nicol", file("."), settings = General.settings ++ Seq (
      mainClass in (Test, run) := Some("nicol.App")
    )
  ) dependsOn nicolCore aggregate (nicolCore)

  lazy val nicolCore = Project(
    "nicol-core", file("core"), settings = General.settings
  )
}

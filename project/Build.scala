import sbt._
import Keys._

object General {
  val settings = Defaults.defaultSettings ++ LWJGLPlugin.lwjglSettings ++ Seq(
    organization := "com.github.scan",
    scalaVersion := "2.9.2",
    version := "0.1.2",
    crossScalaVersions := Seq("2.9.2"),
    credentials += Credentials(Path.userHome / ".ivy2" / ".credentials"),
    libraryDependencies ++= Seq(
      "ch.qos.logback" % "logback-classic" % "1.0.6",
      "org.scalacheck" %% "scalacheck" % "1.10.0" % "test"
    )
  )
}

object NicolEngine extends Build {
  lazy val nicol = Project(
    "nicol", file("."), settings = General.settings ++ Seq (
      mainClass in (Test, run) := Some("nicol.App")
    )
  ) dependsOn nicolCore aggregate (nicolCore, nicolTiles)

  lazy val nicolCore = Project(
    "nicol-core", file("core"), settings = General.settings
  )

  lazy val nicolTiles = Project(
    "nicol-tiles", file("tiles"), settings = General.settings
  )  dependsOn nicolCore
}

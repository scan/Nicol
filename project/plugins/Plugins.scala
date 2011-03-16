import sbt._

class Plugins(info: ProjectInfo) extends PluginDefinition(info) {
  val sbtIdeaRepo = "sbt-idea-repo" at "http://mpeltonen.github.com/maven/"
  val sbtIdea = "com.github.mpeltonen" % "sbt-idea-plugin" % "0.4.0"
  
  val sbtLwjglRepo = "sbt-lwjgl-repo" at "http://scan.github.com/maven"
  val sbtLwjglPlugin = "com.github.scan" % "sbt-lwjgl-plugin" % "0.3.1"
}
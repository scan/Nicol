import sbt._

class NicolProject(info: ProjectInfo) extends LWJGLProject(info) with IdeaProject {
	override def mainClass = Some("com.scan.nicol.App")
}
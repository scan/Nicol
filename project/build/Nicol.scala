import sbt._

class NicolProject(info: ProjectInfo) extends LWJGLProject(info) with IdeaProject {
	override def testMainClass = Some("com.scan.nicol.App")

  val scalacheck = "org.scala-tools.testing" % "scalacheck_2.8.1" % "1.8" % "test"
}
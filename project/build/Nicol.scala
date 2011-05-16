import sbt._

class NicolProject(info: ProjectInfo) extends LWJGLProject(info) with IdeaProject {
	override def testMainClass = Some("com.scan.nicol.App")

	val scalacheck = "org.scala-tools.testing" %% "scalacheck" % "1.8" % "test"
	
	val javaNet = "JavaNet" at "http://download.java.net/maven/2/"	
	val jorbis = "com.projectdarkstar.ext.jorbis" % "jorbis" % "0.0.17"
}

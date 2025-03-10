import sbt._

object Dependencies {
  object Versions {
    val scalaTest = "3.2.18"
  }

  val scalastic = "org.scalactic" %% "scalactic" % Versions.scalaTest
  val scalaTest = "org.scalatest" %% "scalatest" % Versions.scalaTest % "test"
  val catsEffect = "org.typelevel" %% "cats-effect" % "3.5.4"
}

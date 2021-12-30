import mill._, scalalib._

val `2.13` = "2.13.7"
val `3`    = "3.1.0"

object default                               extends mill.Cross[Default](`2.13`, `3`)
class Default(val crossScalaVersion: String) extends CrossScalaModule { self =>
  def scalacOptions =
    Seq(
      "-Xfatal-warnings",
      "-feature",
      "-unchecked",
      "-deprecation"
    ) ++ (
      if (crossScalaVersion == `2.13`)
        Seq("-Ywarn-macros:after", "-Ywarn-unused", "-Xsource:3")
      else None
    )

  object test extends Tests with TestModule.Munit {
    def ivyDeps       = Agg(ivy"org.scalameta::munit:1.0.0-M1")
    def scalacOptions = T(
      self.scalacOptions().filterNot(_ == "-Xfatal-warnings")
    )
  }
}

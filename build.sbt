enablePlugins(ScalaJSPlugin)

name := "ostinato-example"

version := "1.0"

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  "org.gappa" %%% "ostinato" % "0.1-SNAPSHOT",
  "org.scala-js" %%% "scalajs-dom" % "0.8.2"
)

scalaJSStage in Global := FastOptStage
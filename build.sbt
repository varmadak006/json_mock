name := """json-mock"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.7"

libraryDependencies += javaJdbc
libraryDependencies += cache
libraryDependencies += javaWs

libraryDependencies ++= Seq(
  "com.fasterxml.jackson.core" % "jackson-databind" % "2.4.0"
)
// https://mvnrepository.com/artifact/com.googlecode.json-simple/json-simple
libraryDependencies += "com.googlecode.json-simple" % "json-simple" % "1.1"
